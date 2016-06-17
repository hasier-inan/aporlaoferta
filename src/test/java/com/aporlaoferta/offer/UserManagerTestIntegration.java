package com.aporlaoferta.offer;

import com.aporlaoferta.dao.UserDAO;
import com.aporlaoferta.dao.UserRolesDAO;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.data.UserRoleBuilderManager;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.TheUserRoles;
import com.aporlaoferta.model.UserRoles;
import com.aporlaoferta.service.UserManager;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml", "classpath:aporlaoferta-managers-test-context.xml"})
@Transactional
public class UserManagerTestIntegration {

    private static final String NICKNAME = "theNickNam3";

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserRolesDAO userRolesDAO;

    @Autowired
    private UserManager userManager;

    @Before
    public void setUp() {
        this.userDAO.save(UserBuilderManager.aRegularUserWithNickname(NICKNAME).build());
        this.userRolesDAO.save(UserRoleBuilderManager.aRoleUser(NICKNAME).build());
    }

    @Test
    public void testUniqueUserIsInTheInMemoryDatabase() {
        assertThat("Expected only one user to be in the db", this.userDAO.count(), is(1L));
    }

    @Test
    public void testUserIsReceivedFromTheNickName() {
        TheUser theUser = this.userManager.getUserFromNickname(NICKNAME);
        assertNotNull(theUser);
    }

    @Test
    public void testNullUserIsReceivedFromNonExistingNickName() {
        TheUser theUser = this.userManager.getUserFromNickname("dafuQ");
        assertNull(theUser);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testTransitionFailsIfTriesToAddUserWithSameNickname() {
        this.userManager.createUser(UserBuilderManager.aRegularUserWithNickname(NICKNAME).build());
    }

    @Test
    public void testUserIsCheckedInDB() {
        assertTrue(this.userManager.doesUserExist(NICKNAME));
    }

    @Test
    public void testBrandNewUserIsCreatedInBothTables() {
        String nickName = "supermuthaducka2";
        TheUser theUser = UserBuilderManager.aRegularUserWithNickname(nickName).build();
        this.userManager.createUser(theUser);
        assertThat("Expected two results after this creation", this.userDAO.count(), is(2L));
        assertThat("Expected two results after this creation", this.userRolesDAO.count(), is(2L));
        List<TheUserRoles> theUserRoles = this.userRolesDAO.findByUserNickname(nickName);
        assertThat(theUserRoles.get(0).getUserRole(), is(UserRoles.ROLE_USER));
        TheUser sameUser = this.userManager.getUserFromNickname(nickName);
        assertNotNull(sameUser);
    }

    @Test
    public void testUserIsUpdatedWithLatestValues() {
        String oldPass = UserBuilderManager.aRegularUserWithNickname(NICKNAME).build().getUserPassword();
        String theNewPass = "thenewAdfklsdfañ";
        String theNewAvatar = "nedsjlsdfj";
        String theNewEmail = "sdfadf@sadef.com";
        TheUser theUser = UserBuilderManager.aRegularUserWithNickname(NICKNAME).build();
        theUser.setUserPassword(theNewPass);
        theUser.setUserAvatar(theNewAvatar);
        theUser.setUserEmail(theNewEmail);
        TheUser userUpdated = this.userManager.updateUser(theUser, false);
        assertThat("Expected the new email", userUpdated.getUserEmail(), is(theNewEmail));
        assertThat("Expected the new avatar", userUpdated.getUserAvatar(), is(theNewAvatar));
        assertFalse(oldPass.equals(userUpdated.getUserPassword()));
    }

    @Test
    public void testAdminUserIsCorrectlyCreated() {
        String userNickname = "imtheboss";
        TheUser admin = this.userManager.createAdminUser(UserBuilderManager.aRegularUserWithNickname(userNickname).build());
        assertNotNull(admin);
        List<TheUserRoles> adminRoles = this.userRolesDAO.findByUserNickname(userNickname);
        assertThat("Expected the admin role to be in the user roles", adminRoles.get(0).getUserRole(), Matchers.is(UserRoles.ROLE_ADMIN));
    }


}

