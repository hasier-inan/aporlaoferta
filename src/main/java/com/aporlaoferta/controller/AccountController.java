package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheDefaultOffer;
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

    @RequestMapping("favicon.ico")
    String favicon() {
        return "forward:/resources/images/favicon.ico";
    }

    @RequestMapping(value = {"/", "/start**", "/index**"}, method = RequestMethod.GET)
    public ModelAndView start() {
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public ModelAndView adminPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Login Form - Database Authentication");
        model.addObject("message", "This page is for ROLE_ADMIN only!");
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
        if (this.captchaHttpManager.validHuman(reCaptcha)) {
            return processUserCreation(theUser);
        } else {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        }

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse updateUser(@RequestBody TheUser theNewUser,
                                  @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (this.captchaHttpManager.validHuman(reCaptcha)) {
            return processUserUpdate(theNewUser);
        } else {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        }
    }

    private TheResponse processUserUpdate(TheUser theNewUser) {
        TheResponse result = new TheResponse();
        String userNickname = this.userManager.getUserNickNameFromSession();
        updateNewestAvatar(theNewUser, userNickname);
        theNewUser.setUserNickname(userNickname);
        if (isEmpty(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_IS_INVALID);
        } else if (!this.userManager.doesUserExist(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_DOES_NOT_EXIST);
        } else {
            validateAndUpdateUser(theNewUser, result, userNickname);
        }
        return result;
    }

    private void updateNewestAvatar(TheUser theNewUser, String userNickname) {
        TheUser theOldUser = this.userManager.getUserFromNickname(userNickname);
        if (isEmpty(theNewUser.getUserAvatar())) {
            theNewUser.setUserAvatar(theOldUser.getUserAvatar());
        }
    }

    private void validateAndUpdateUser(TheUser theNewUser, TheResponse result, String userNickname) {
        try {
            validatePassword(theNewUser);
            this.offerValidatorHelper.validateUser(theNewUser);
            TheUser nuUser = this.userManager.updateUser(theNewUser);
            if (nuUser == null) {
                ControllerHelper.addEmptyDatabaseObjectMessage(result, ". Nickname: " + userNickname, LOG);
            } else {
                String okMessage = String.format("User successfully updated. Id: %s", nuUser.getId());
                LOG.info(okMessage);
                result.assignResultCode(ResultCode.ALL_OK, okMessage, "Usuario actualizado satisfactoriamente");
            }
        } catch (ValidationException e) {
            String resultDescription = ResultCode.UPDATE_USER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.UPDATE_USER_VALIDATION_ERROR);
        }
    }

    private TheResponse processUserCreation(TheUser theUser) {
        TheResponse result = new TheResponse();
        String userNickname = theUser.getUserNickname();
        addDefaultAvatar(theUser);
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
            return theUser;
        }
    }

    private void validatePassword(TheUser theUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        theUser.setUserPassword(bCryptPasswordEncoder.encode(theUser.getUserPassword()));
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
                result.assignResultCode(ResultCode.ALL_OK, okMessage, "Usuario creado satisfactoriamente");
            }
        } catch (ValidationException e) {
            String resultDescription = ResultCode.CREATE_USER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.CREATE_USER_VALIDATION_ERROR);
        }
    }
}
