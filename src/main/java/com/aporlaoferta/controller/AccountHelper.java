package com.aporlaoferta.controller;

import com.aporlaoferta.controller.helpers.DatabaseHelper;
import com.aporlaoferta.email.EmailSendingException;
import com.aporlaoferta.email.EmailService;
import com.aporlaoferta.model.TheDefaultOffer;
import com.aporlaoferta.model.TheEnhancedUser;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CaptchaHTTPManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 24/02/2017
 * Time: 17:08
 */
public class AccountHelper {
    protected final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected OfferValidatorHelper offerValidatorHelper;

    @Autowired
    protected CaptchaHTTPManager captchaHttpManager;

    @Autowired
    protected EmailService emailService;

    protected TheResponse processUserUpdate(TheEnhancedUser theEnhancedUser) {
        TheResponse result = new TheResponse();
        String userNickname = this.userManager.getUserNickNameFromSession();
        TheUser theOldUser = this.userManager.getUserFromNickname(userNickname);

        updateDefaultUserValues(theEnhancedUser, userNickname, theOldUser);
        if (!verifyOldPasswordAndUpdateNewestAvatar(theEnhancedUser, theOldUser)) {
            result.assignResultCode(ResultCode.USER_NAME_PASSWORD_INVALID);
            return result;
        }
        if (isEmpty(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_IS_INVALID);
        } else if (!this.userManager.doesUserExist(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_DOES_NOT_EXIST);
        } else {
            try {
                this.offerValidatorHelper.validateUser(theEnhancedUser);
                TheUser nuUser = this.userManager.updateUser(theEnhancedUser, theOldUser, passwordUpdateIsNotRequired(theEnhancedUser));
                updatedUserResponse(result, userNickname, nuUser);
            } catch (ValidationException e) {
                return ResponseResultHelper.userUpdatedResponse(result, e);
            }
        }
        return result;
    }

    protected void updateDefaultUserValues(TheEnhancedUser theEnhancedUser, String userNickname, TheUser theUser) {
        theEnhancedUser.setUserNickname(userNickname);
        String specifiedEmail = theEnhancedUser.getUserSpecifiedEmail();
        if (!isEmpty(specifiedEmail)) {
            theEnhancedUser.setUserEmail(specifiedEmail);
        } else {
            theEnhancedUser.setUserEmail(theUser.getUserEmail());
        }
    }

    protected boolean verifyOldPasswordAndUpdateNewestAvatar(TheEnhancedUser theEnhancedUser, TheUser theOldUser) {
        verifyAvatar(theEnhancedUser, theOldUser);
        return verifyOldPassword(theEnhancedUser, theOldUser);
    }

    protected boolean verifyOldPassword(TheEnhancedUser theEnhancedUser, TheUser theOldUser) {
        if (passwordUpdateIsNotRequired(theEnhancedUser)) {
            theEnhancedUser.setUserPassword(theOldUser.getUserPassword());
            return true;
        } else {
            theEnhancedUser.setUserPassword(theEnhancedUser.getUserSpecifiedPassword());
            validatePassword(theEnhancedUser);
        }
        return isSamePersistedPassword(theEnhancedUser, theOldUser);
    }

    protected void verifyAvatar(TheEnhancedUser theEnhancedUser, TheUser theOldUser) {
        if (isEmpty(theEnhancedUser.getUserAvatar())) {
            theEnhancedUser.setUserAvatar(theOldUser.getUserAvatar());
        }
    }

    protected boolean passwordUpdateIsNotRequired(TheEnhancedUser theEnhancedUser) {
        return isEmpty(theEnhancedUser.getOldPassword()) && isEmpty(theEnhancedUser.getUserSpecifiedPassword());
    }

    protected boolean isSamePersistedPassword(TheEnhancedUser theEnhancedUser, TheUser theOldUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        String oldPassword = theEnhancedUser.getOldPassword();
        return !isEmpty(oldPassword) && bCryptPasswordEncoder.matches(oldPassword, theOldUser.getUserPassword());
    }

    protected void updatedUserResponse(TheResponse result, String userNickname, TheUser nuUser) {
        if (nuUser == null) {
            DatabaseHelper.addEmptyDatabaseObjectMessage(result, ". Nickname: " + userNickname, LOG);
        } else {
            String okMessage = String.format("User successfully updated. Id: %s", nuUser.getId());
            LOG.info(okMessage);
            result.assignResultCode(ResultCode.ALL_OK, okMessage, "Usuario actualizado");
        }
    }

    protected TheResponse processUserCreation(TheEnhancedUser theUser) {
        TheResponse result = new TheResponse();
        String userNickname = theUser.getUserNickname();
        if (isEmpty(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_IS_INVALID);
        } else if (this.userManager.doesUserExist(userNickname)) {
            result.assignResultCode(ResultCode.USER_NAME_ALREADY_EXISTS);
        } else {
            TheUser defaultUser = createDefaultUserValues(theUser);
            validatePassword(defaultUser);
            validateAndCreateUser(defaultUser, result, userNickname);
        }
        return result;
    }

    protected TheUser createDefaultUserValues(TheEnhancedUser theUser) {
        TheUser defaultUser = new TheUser();
        defaultUser.setUserPassword(theUser.getUserSpecifiedPassword());
        defaultUser.setUserEmail(theUser.getUserSpecifiedEmail());
        defaultUser.setUserNickname(theUser.getUserNickname());
        addDefaultAvatar(defaultUser);
        addPendingFlag(defaultUser);
        return defaultUser;
    }

    protected void addPendingFlag(TheUser theUser) {
        theUser.setPending(true);
        theUser.setUuid(UUID.randomUUID().toString());
    }

    protected void addDefaultAvatar(TheUser theUser) {
        if (isEmpty(theUser.getUserAvatar())) {
            theUser.setUserAvatar(TheDefaultOffer.AVATAR_IMAGE_URL.getCode());
        }
    }

    protected void validatePassword(TheUser theUser) {
        theUser.setUserPassword(encryptPassword(theUser.getUserPassword()));
    }

    protected String encryptPassword(String original) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        return bCryptPasswordEncoder.encode(original);
    }

    protected void validateAndCreateUser(TheUser theUser, TheResponse result, String userNickname) {
        try {
            this.offerValidatorHelper.validateUser(theUser);
            TheUser user = this.userManager.createUser(theUser);
            if (user == null) {
                DatabaseHelper.addEmptyDatabaseObjectMessage(result, ". Nickname: " + userNickname, LOG);
            } else {
                String okMessage = String.format("User successfully created. Id: %s", user.getId());
                LOG.info(okMessage);
                result.assignResultCode(ResultCode.ALL_OK, okMessage, "Confirma tu cuenta en el correo electrónico que te hemos enviado");
                this.emailService.sendAccountConfirmationEmail(user);
            }
        } catch (EmailSendingException | ValidationException e) {
            String resultDescription = ResultCode.CREATE_USER_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.CREATE_USER_VALIDATION_ERROR);
        }
    }

    protected void sendConfirmationEmail(TheUser userFromEmail) {
        try {
            this.emailService.sendAccountConfirmationEmail(userFromEmail);
        } catch (EmailSendingException e) {
            LOG.error("Could not send confirmarion email: ", e.getMessage());
        }
    }
}
