package com.aporlaoferta.controller;

import com.aporlaoferta.email.EmailSendingException;
import com.aporlaoferta.model.TheEnhancedUser;
import com.aporlaoferta.model.TheForgettableUser;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class AccountController extends AccountHelper {

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
            model.addObject("msg", isEmpty(error) ? "Los datos introducidos son invalidos" : error);
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
    public TheResponse createUser(@RequestBody TheEnhancedUser theUser,
                                  @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (!this.captchaHttpManager.validHuman(reCaptcha)) {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        } else if (this.userManager.doesUserEmailExist(theUser.getUserSpecifiedEmail())) {
            return ResponseResultHelper.createExistingEmailResponse();
        } else {
            return processUserCreation(theUser);
        }

    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse updateUser(@RequestBody TheEnhancedUser theEnhancedUser,
                                  @RequestParam(value = "recaptcha", required = true) String reCaptcha) {
        if (!this.captchaHttpManager.validHuman(reCaptcha)) {
            return ResponseResultHelper.createInvalidCaptchaResponse();
        }
        if (this.userManager.userIsBanned()) {
            return ResponseResultHelper.createInvalidUserResponse();
        }
        return processUserUpdate(theEnhancedUser);
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
        if (theUser == null || isEmpty(theForgettableUser.getTrack()) ||
                !theForgettableUser.getTrack().equals(theUser.getUuid())) {
            return ResponseResultHelper.updateWithCode(ResultCode.INVALID_PASSWORD_VERIFICATION);
        }
        theUser.setUserPassword(theForgettableUser.getFirstPassword());

        TheResponse result = new TheResponse();
        try {
            validatePassword(theUser);
            this.offerValidatorHelper.validateUser(theUser);
            TheUser nuUser = this.userManager.updateUser(theUser, false);
            updatedUserResponse(result, theForgettableUser.getSecondPassword(), nuUser);
        } catch (ValidationException e) {
            return ResponseResultHelper.userUpdatedResponse(result, e);
        }
        return result;
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

}
