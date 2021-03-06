package com.aporlaoferta.service;

import com.aporlaoferta.controller.ResponseResultHelper;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.TheUserRoles;
import com.aporlaoferta.model.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
public class UserManager {

    private final Logger LOG = LoggerFactory.getLogger(UserManager.class);

    private TransactionalManager transactionalManager;

    public TheUser getUserFromNickname(String nickname) {
        try {
            return this.transactionalManager.getUserFromNickname(nickname);
        } catch (IllegalArgumentException e) {
            LOG.error("Got a null nickname while looking for a user ", e);
        }
        return null;
    }

    public TheUser getUserFromEmail(String email) {
        try {
            return this.transactionalManager.getUserFromEmail(email);
        } catch (IllegalArgumentException e) {
            LOG.error("Got a null email while looking for a user ", e);
        }
        return null;
    }

    /**
     * Creates a new user in the database for given domain object. Includes the user role creation.
     *
     * @param theUser: User to be created
     * @return created user
     */
    public TheUser createUser(TheUser theUser) {
        if (this.transactionalManager.getUserFromNickname(theUser.getUserNickname()) != null) {
            throw new DataIntegrityViolationException("User nickname is in use, can't create new user");
        }
        theUser.setEnabled(true);
        saveUser(theUser);
        TheUserRoles theUserRoles = getTheUserRoles(theUser);
        this.transactionalManager.saveUserRole(theUserRoles);
        return theUser;
    }

    public TheUserRoles getTheUserRoles(TheUser theUser) {
        TheUserRoles theUserRoles = new TheUserRoles();
        theUserRoles.setUserNickname(theUser.getUserNickname());
        theUserRoles.setUserRole(UserRoles.ROLE_USER);
        return theUserRoles;
    }

    public TheUser createAdminUser(TheUser adminUser) {
        adminUser.setEnabled(true);
        saveUser(adminUser);
        TheUserRoles theUserRoles = getTheAdminUserRoles(adminUser);
        this.transactionalManager.saveUserRole(theUserRoles);
        return adminUser;
    }

    public TheUserRoles getTheAdminUserRoles(TheUser theUser) {
        TheUserRoles theUserRoles = new TheUserRoles();
        theUserRoles.setUserNickname(theUser.getUserNickname());
        theUserRoles.setUserRole(UserRoles.ROLE_ADMIN);
        return theUserRoles;
    }

    public TheUser saveUser(TheUser theUser) {
        return this.transactionalManager.saveUser(theUser);
    }

    /**
     * Updates an existing user with newest atributes from theNewUser, including the unencoded pass
     *
     * @param theNewUser: User object with newest data
     * @return original User object with updated details
     */
    public TheUser updateUser(TheUser theNewUser, TheUser theUser, Boolean passwordIsPopulated) {
        return updateUserFromNewOne(theNewUser, passwordIsPopulated, theUser);
    }

    public TheUser updateUser(TheUser theNewUser, Boolean passwordIsPopulated) {
        TheUser theUser = getUserFromNickname(theNewUser.getUserNickname());
        return updateUserFromNewOne(theNewUser, passwordIsPopulated, theUser);
    }

    private TheUser updateUserFromNewOne(TheUser theNewUser, Boolean passwordIsPopulated, TheUser theUser) {
        if (theUser != null) {
            theUser.setUserAvatar(theNewUser.getUserAvatar());
            theUser.setUserEmail(theNewUser.getUserEmail());
            if (!passwordIsPopulated) {
                theUser.setUserPassword(theNewUser.getUserPassword());
            }
            theUser.setUuid(UUID.randomUUID().toString());
            return saveUser(theUser);
        }
        return theUser;
    }

    public TheResponse confirmUser(String uuid, String nickname) {
        TheUser theUser = getUserFromNickname(nickname);
        if (isEmpty(theUser) || !uuid.equals(theUser.getUuid())) {
            return ResponseResultHelper.createInvalidUUIDResponse();
        } else {
            theUser.setPending(false);
            theUser.setUuid(UUID.randomUUID().toString());
            saveUser(theUser);
            return ResponseResultHelper.createUserConfirmationResponse();
        }
    }

    public TheResponse banUser(TheUser theUser) {
        theUser.setEnabled(false);
        theUser.setUuid(UUID.randomUUID().toString());
        saveUser(theUser);
        return ResponseResultHelper.createUserBannedConfirmationResponse();
    }

    public boolean doesUserEmailExist(String userEmail) {
        return !isEmpty(getUserFromEmail(userEmail));
    }

    public boolean doesUserExist(String nickname) {
        try {
            return this.transactionalManager.getUserFromNickname(nickname) != null;
        } catch (IllegalArgumentException e) {
            //null nickname
            LOG.error("Got a null nickname while looking for a user ", e);
        }
        return false;
    }

    public boolean isUserPending(TheUser theUser) {
        return !isEmpty(theUser.getPending()) && theUser.getPending();
    }

    /**
     * Get user name based on current spring - security user session
     *
     * @return (unique) user's nickname
     */
    public String getUserNickNameFromSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theLoggedUser = (User) authentication.getPrincipal();
        return theLoggedUser.getUsername();
    }

    public boolean isUserAuthorised(TheOffer theOffer) {
        String originalNickname = theOffer.getOfferUser().getUserNickname();
        String sessionNickname = getUserNickNameFromSession();
        UserRoles userRole = this.transactionalManager.getUserRoleFromNickname(sessionNickname).get(0).getUserRole();
        return originalNickname.equals(sessionNickname) || UserRoles.ROLE_ADMIN.equals(userRole);
    }

    public boolean userIsBanned() {
        String sessionNickname = getUserNickNameFromSession();
        TheUser theUser = getUserFromNickname(sessionNickname);
        return !theUser.getEnabled();
    }

    public boolean isUserAdmin() {
        String sessionNickname = getUserNickNameFromSession();
        UserRoles userRole = this.transactionalManager.getUserRoleFromNickname(sessionNickname).get(0).getUserRole();
        return UserRoles.ROLE_ADMIN.equals(userRole);
    }

    @Autowired
    public void setTransactionalManager(TransactionalManager transactionalManager) {
        this.transactionalManager = transactionalManager;
    }
}
