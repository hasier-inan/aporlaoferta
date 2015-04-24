package com.aporlaoferta.service;

import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.TheUserRoles;
import com.aporlaoferta.model.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by hasiermetal on 15/01/15.
 */
@Service
public class UserManager {
    private final Logger LOG = LoggerFactory.getLogger(UserManager.class);

    TransactionalManager transactionalManager;

    public TheUser getUserFromNickname(String nickname) {
        try {
            return this.transactionalManager.getUserFromNickname(nickname);
        } catch (IllegalArgumentException e) {
            //null nickname
            LOG.error("Got a null nickname while looking for a user ", e);
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
    public TheUser updateUser(TheUser theNewUser) {
        TheUser theUser = getUserFromNickname(theNewUser.getUserNickname());
        if (theUser != null) {
            theUser.setUserAvatar(theNewUser.getUserAvatar());
            theUser.setUserEmail(theNewUser.getUserEmail());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            theUser.setUserPassword(passwordEncoder.encode(theNewUser.getUserPassword()));
            return saveUser(theUser);
        }
        return theUser;
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

    @Autowired
    public void setTransactionalManager(TransactionalManager transactionalManager) {
        this.transactionalManager = transactionalManager;
    }
}
