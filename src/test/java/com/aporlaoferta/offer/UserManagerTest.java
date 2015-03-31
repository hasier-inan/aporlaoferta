package com.aporlaoferta.offer;

import com.aporlaoferta.dao.UserDAO;
import com.aporlaoferta.dao.UserRolesDAO;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.TheUserRoles;
import com.aporlaoferta.model.UserRoles;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserManagerTest {

    private final static String NICKNAME = "theNickNam3";

    @InjectMocks
    UserManager userManager;

    @Mock
    UserDAO userDAO;

    @Mock
    UserRolesDAO userRolesDAO;

    TheUser theUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.theUser = UserBuilderManager.aRegularUserWithNickname(NICKNAME).build();
        Mockito.when(this.userDAO.findByUserNickname(NICKNAME)).thenReturn(theUser);
    }

    @Test
    public void testUserDaoFindsOneUniqueUserFromNickname() {
        assertNotNull(this.userManager.getUserFromNickname(NICKNAME));
        verify(this.userDAO).findByUserNickname(NICKNAME);
    }

    @Test
    public void testUserRolesAreCreatedForANewUser() {
        TheUserRoles theUserRoles = this.userManager.getTheUserRoles(this.theUser);
        assertThat("expected user role to be ROLE_USER", theUserRoles.getUserRole(), Matchers.is(UserRoles.ROLE_USER));
        assertThat("expected user nickname to be the same", theUserRoles.getUserNickname(), Matchers.is(NICKNAME));
    }

    @Test
    public void testUserIsCreatedUsingDaos() {
        this.userManager.createUser(this.theUser);
        verify(this.userDAO).save(this.theUser);
        verify(this.userRolesDAO).save(any(TheUserRoles.class));
    }

    @Test
    public void testInvalidIdThrowsExpectedExceptionAndThusReturnsNull() {
        assertNull(this.userManager.getUserFromNickname(null));
    }

    @Test
    public void testUserUpdateSavesUserAndUpdatesAllData() {
        TheUser theNewestUser = UserBuilderManager.aRegularUserWithNickname(NICKNAME).build();
        String theNewPass = "abrandnewduckingpassword";
        theNewestUser.setUserPassword(theNewPass);
        this.userManager.updateUser(theNewestUser);
        //this.theUser will be the user model that will be updated with new password etc.
        verify(this.userDAO).save(this.theUser);
        //password updated with the encoded of the new pass
        assertThat(this.theUser.getUserPassword().length(), Matchers.is(60));
    }
}

