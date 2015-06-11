package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    OfferValidatorHelper offerValidatorHelper;

    @RequestMapping(value = {"/","/start**"}, method = RequestMethod.GET)
    public ModelAndView start() {
        ModelAndView model = new ModelAndView();
        model.setViewName("start");
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
            model.addObject("error", "Invalid username and password!");
        }
        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("login");
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
    public TheResponse createUser(@RequestBody TheUser theUser) {
        TheResponse result = new TheResponse();
        String userNickname = theUser.getUserNickname();
        if (isEmpty(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_IS_INVALID);
        } else if (this.userManager.doesUserExist(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_ALREADY_EXISTS);
        } else {
            validateAndCreateUser(theUser, result, userNickname);
        }
        return result;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse updateUser(@RequestBody TheUser theNewUser) {
        TheResponse result = new TheResponse();
        String userNickname = this.userManager.getUserNickNameFromSession();
        theNewUser.setUserNickname(userNickname);
        if (isEmpty(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_IS_INVALID);
        } else if (!this.userManager.doesUserExist(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_DOES_NOT_EXIST);
        } else {
            try {
                this.offerValidatorHelper.validateUser(theNewUser);
                TheUser nuUser = this.userManager.updateUser(theNewUser);
                if (nuUser == null) {
                    ControllerHelper.addEmptyDatabaseObjectMessage(result, ". Nickname: " + userNickname, LOG);
                } else {
                    String okMessage = String.format("User successfully updated. Id: %s", nuUser.getId());
                    LOG.info(okMessage);
                    result.assignResultCode(ResultCode.ALL_OK, okMessage);
                }
            } catch (ValidationException e) {
                String resultDescription = ResultCode.UPDATE_USER_VALIDATION_ERROR.getResultDescription();
                LOG.warn(resultDescription, e);
                result.assignResultCode(ResultCode.UPDATE_USER_VALIDATION_ERROR);
            }
        }
        return result;
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
                result.assignResultCode(ResultCode.ALL_OK,okMessage);
            }
        } catch (ValidationException e) {
            String resultDescription = ResultCode.CREATE_USER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.CREATE_USER_VALIDATION_ERROR);
        }
    }
}
