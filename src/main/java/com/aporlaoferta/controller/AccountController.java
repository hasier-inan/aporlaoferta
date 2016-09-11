package com.aporlaoferta.controller;

import com.aporlaoferta.email.EmailSendingException;
import com.aporlaoferta.email.EmailService;
import com.aporlaoferta.model.TheDefaultOffer;
import com.aporlaoferta.model.TheForgettableUser;
import com.aporlaoferta.model.TheNewUser;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CaptchaHTTPManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 18/01/15.
 */
@Controller
public class AccountController {

    private final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private OfferValidatorHelper offerValidatorHelper;

    @Autowired
    private CaptchaHTTPManager captchaHttpManager;

    @Autowired
    private EmailService emailService;

    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/resources/images/favicon.ico";
    }

    @RequestMapping(value = {"/tc"}, method = RequestMethod.GET)
    public ModelAndView termsAndConditions() {
        ModelAndView tc = new ModelAndView();
        tc.setViewName("termsAndConditions");
        return tc;
    }

    @RequestMapping(value = {"/cp"}, method = RequestMethod.GET)
    public ModelAndView cookiePolicies() {
        ModelAndView tc = new ModelAndView();
        tc.setViewName("cookieTerms");
        return tc;
    }

    @RequestMapping(value = {"/", "/start**", "/index**"}, method = RequestMethod.GET)
    public ModelAndView start(@RequestParam(value = "sh", required = false) Long number) {
        ModelAndView model = new ModelAndView();
        if (!isEmpty(number)) {
            model.addObject("specificOffer", number);
        }
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public ModelAndView adminPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Login Form - Database Authentication");
        model.addObject("message", "This page is for admins only!");
        model.setViewName("admin");
        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("msg", "Los datos introducidos son invalidos");
        }
        if (logout != null) {
            model.addObject("msg", "Se ha cerrado la sesión");
        }
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied() {
        ModelAndView model = new ModelAndView();
        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addObject("username", userDetail.getUsername());
        }
        model.setViewName("403");
        return model;
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse createUser(@RequestBody TheUser theUser,
                                  @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (!this.captchaHttpManager.validHuman(reCaptcha)) {
            return ResponseResultHelper.createInvalidCaptchaResponse();

        } else if (this.userManager.doesUserEmailExist(theUser.getUserEmail())) {
            return ResponseResultHelper.createExistingEmailResponse();
        } else {
            return processUserCreation(theUser);
        }

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse updateUser(@RequestBody TheNewUser theNewUser,
                                  @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (!this.captchaHttpManager.validHuman(reCaptcha)) {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        }
        if(this.userManager.userIsBanned()){
            return ResponseResultHelper.createInvalidUserResponse();
        }
        return processUserUpdate(theNewUser);
    }

    @RequestMapping(value = "/banUser", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse banUser(@RequestParam(value = "nickname", required = true) String nickname) {
        if (!this.userManager.isUserAdmin()) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.DEFAULT_ERROR, new TheResponse());
        }
        TheUser theUser = this.userManager.getUserFromNickname(nickname);
        if (theUser == null) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.UPDATE_USER_VALIDATION_ERROR, new TheResponse());
        }
        return this.userManager.banUser(theUser);
    }

    @RequestMapping(value = "/requestForgottenPassword", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse requestForgottenPassword(@RequestParam(value = "userEmail", required = true) String userEmail) {
        TheResponse theResponse = new TheResponse();
        TheUser userFromEmail = this.userManager.getUserFromEmail(userEmail);
        if (isEmpty(userFromEmail) || !userFromEmail.getEnabled()) {
            return ResponseResultHelper.responseResultWithResultCodeError(
                    ResultCode.USER_EMAIL_IS_INVALID, theResponse);
        }
        if (userFromEmail.getPending()) {
            sendConfirmationEmail(userFromEmail);
            return ResponseResultHelper.responseResultWithResultCodeError(
                    ResultCode.USER_EMAIL_NOT_CONFIRMED, theResponse);
        }
        try {
            this.emailService.sendPasswordForgotten(userFromEmail);
        } catch (EmailSendingException e) {
            LOG.warn("Could not send password forgotten email: ", e.getMessage());
            return ResponseResultHelper.createDefaultResponse();
        }
        return ResponseResultHelper.createForgottenPasswordResponse();
    }

    @RequestMapping(value = "/forgottenPassword", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse passwordUpdate(@RequestBody TheForgettableUser theForgettableUser) {
        TheUser theUser = this.userManager.getUserFromNickname(theForgettableUser.getUserNickname());
        if (!theForgettableUser.passMatches()) {
            return ResponseResultHelper.updateWithCode(ResultCode.USER_NAME_PASSWORD_INVALID);
        }
        if (theUser == null || !theForgettableUser.getUuid().equals(theUser.getUuid())) {
            return ResponseResultHelper.updateWithCode(ResultCode.INVALID_PASSWORD_VERIFICATION);
        }
        theUser.setUserPassword(theForgettableUser.getFirstPassword());
        return validateAndUpdateUser(theUser, new TheResponse(), theForgettableUser.getSecondPassword(), false);
    }

    @RequestMapping(value = {"/passwordForgotten**"}, method = RequestMethod.GET)
    public ModelAndView passwordForgotten(@RequestParam(value = "user", required = false) String user,
                                          @RequestParam(value = "track", required = false) String uuid) {
        ModelAndView model = new ModelAndView();
        model.setViewName("passwordForgotten");
        if (!isEmpty(user) && !isEmpty(uuid)) {
            model.addObject("nick", user);
            model.addObject("uuid", uuid);
        }
        return model;
    }

    private TheResponse processUserUpdate(TheNewUser theNewUser) {
        TheResponse result = new TheResponse();
        String userNickname = this.userManager.getUserNickNameFromSession();
        theNewUser.setUserNickname(userNickname);
        Boolean passwordPopulated = passwordUpdateIsNotRequired(theNewUser);
        if (!verifyOldPasswordAndUpdateNewestAvatar(theNewUser, userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_PASSWORD_INVALID);
            return result;
        }
        if (isEmpty(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_IS_INVALID);
        } else if (!this.userManager.doesUserExist(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_DOES_NOT_EXIST);
        } else {
            validateAndUpdateUser(theNewUser, result, userNickname, passwordPopulated);
        }
        return result;
    }

    private boolean verifyOldPasswordAndUpdateNewestAvatar(TheNewUser theNewUser, String userNickname) {
        TheUser theOldUser = this.userManager.getUserFromNickname(userNickname);
        if (isEmpty(theNewUser.getUserAvatar())) {
            theNewUser.setUserAvatar(theOldUser.getUserAvatar());
        }
        if (passwordUpdateIsNotRequired(theNewUser)) {
            theNewUser.setUserPassword(theOldUser.getUserPassword());
            return true;
        }
        return isSamePersistedPassword(theNewUser, theOldUser);
    }

    private boolean passwordUpdateIsNotRequired(TheNewUser theNewUser) {
        return isEmpty(theNewUser.getOldPassword()) && isEmpty(theNewUser.getUserPassword());
    }

    private boolean isSamePersistedPassword(TheNewUser theNewUser, TheUser theOldUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        String oldPassword = theNewUser.getOldPassword();
        return !isEmpty(oldPassword) && bCryptPasswordEncoder.matches(oldPassword, theOldUser.getUserPassword());
    }

    private TheResponse validateAndUpdateUser(TheUser theNewUser, TheResponse result, String userNickname, boolean passwordIsPopulated) {
        try {
            validatePassword(theNewUser);
            this.offerValidatorHelper.validateUser(theNewUser);
            TheUser nuUser = this.userManager.updateUser(theNewUser, passwordIsPopulated);
            if (nuUser == null) {
                ControllerHelper.addEmptyDatabaseObjectMessage(result, ". Nickname: " + userNickname, LOG);
            } else {
                String okMessage = String.format("User successfully updated. Id: %s", nuUser.getId());
                LOG.info(okMessage);
                result.assignResultCode(ResultCode.ALL_OK, okMessage, "Usuario actualizado");
            }
        } catch (ValidationException e) {
            String resultDescription = ResultCode.UPDATE_USER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.UPDATE_USER_VALIDATION_ERROR);
        }
        return result;
    }

    private TheResponse processUserCreation(TheUser theUser) {
        TheResponse result = new TheResponse();
        String userNickname = theUser.getUserNickname();
        addDefaultAvatar(theUser);
        addPendingFlag(theUser);
        if (isEmpty(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_IS_INVALID);
        } else if (this.userManager.doesUserExist(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_ALREADY_EXISTS);
        } else {
            validatePassword(theUser);
            validateAndCreateUser(theUser, result, userNickname);
        }
        return result;
    }

    private void addPendingFlag(TheUser theUser) {
        theUser.setPending(true);
        theUser.setUuid(UUID.randomUUID().toString());
    }

    private void addDefaultAvatar(TheUser theUser) {
        if (isEmpty(theUser.getUserAvatar())) {
            theUser.setUserAvatar(TheDefaultOffer.AVATAR_IMAGE_URL.getCode());
        }
    }

    @RequestMapping(value = "/accountDetails", method = RequestMethod.POST)
    @ResponseBody
    public TheUser accountDetails() {
        String userNickname = this.userManager.getUserNickNameFromSession();
        if (isEmpty(userNickname)) {
            return new TheUser();
        } else {
            TheUser theUser = this.userManager.getUserFromNickname(userNickname);
            theUser.setUserPassword("");
            theUser.setUuid("");
            return theUser;
        }
    }

    @RequestMapping(value = "/confirmUser", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView confirmUser(@RequestParam(value = "user", required = true) String nickname,
                                    @RequestParam(value = "confirmationID", required = true) String uuid) {
        TheResponse theResponse;
        if (!isEmpty(uuid) && !isEmpty(nickname)) {
            theResponse = this.userManager.confirmUser(uuid, nickname);
        } else {
            theResponse = ResponseResultHelper.createInvalidUUIDResponse();
        }
        ModelAndView model = new ModelAndView();
        model.addObject("msg", theResponse.getDescriptionEsp());
        model.setViewName("index");
        return model;
    }

    private void validatePassword(TheUser theUser) {
        theUser.setUserPassword(encryptPassword(theUser.getUserPassword()));
    }

    private String encryptPassword(String original) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        return bCryptPasswordEncoder.encode(original);
    }

    private void validateAndCreateUser(TheUser theUser, TheResponse result, String userNickname) {
        try {
            this.offerValidatorHelper.validateUser(theUser);
            TheUser user = this.userManager.createUser(theUser);
            if (user == null) {
                ControllerHelper.addEmptyDatabaseObjectMessage(result, ". Nickname: " + userNickname, LOG);
            } else {
                String okMessage = String.format("User successfully created. Id: %s", user.getId());
                LOG.info(okMessage);
                result.assignResultCode(ResultCode.ALL_OK, okMessage, "Usuario creado satisfactoriamente, se ha enviado un correo electrónico para verificar la cuenta");
                this.emailService.sendAccountConfirmationEmail(user);
            }
        } catch (EmailSendingException | ValidationException e) {
            String resultDescription = ResultCode.CREATE_USER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.CREATE_USER_VALIDATION_ERROR);
        }
    }

    private void sendConfirmationEmail(TheUser userFromEmail) {
        try {
            this.emailService.sendAccountConfirmationEmail(userFromEmail);
        } catch (EmailSendingException e) {
            LOG.error("Could not send confirmarion email: ", e.getMessage());
        }
    }
}
