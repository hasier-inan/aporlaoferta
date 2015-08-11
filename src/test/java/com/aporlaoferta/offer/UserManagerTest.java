package com.aporlaoferta.offer;

import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.TheUserRoles;
import com.aporlaoferta.model.UserRoles;
import com.aporlaoferta.service.TransactionalManager;
import com.aporlaoferta.service.UserManager;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserManagerTest {

    private final static String NICKNAME = "theNickNam3";

    @InjectMocks
    UserManager userManager;

    @Mock
    TransactionalManager transactionalManager;

    TheUser theUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.theUser = UserBuilderManager.aRegularUserWithNickname(NICKNAME).build();
        Mockito.when(this.transactionalManager.getUserFromNickname(NICKNAME)).thenReturn(theUser);
    }

    @Test
    public void testUserDaoFindsOneUniqueUserFromNickname() {
        assertNotNull(this.userManager.getUserFromNickname(NICKNAME));
        verify(this.transactionalManager).getUserFromNickname(NICKNAME);
    }

    @Test
    public void testUserRolesAreCreatedForANewUser() {
        TheUserRoles theUserRoles = this.userManager.getTheUserRoles(this.theUser);
        assertThat("expected user role to be ROLE_USER", theUserRoles.getUserRole(), Matchers.is(UserRoles.ROLE_USER));
        assertThat("expected user nickname to be the same", theUserRoles.getUserNickname(), Matchers.is(NICKNAME));
    }

    @Test
    public void testUserIsCreatedUsingDaosIfUsernameDoesNotExist() {
        Mockito.when(this.transactionalManager.getUserFromNickname(NICKNAME)).thenReturn(null);
        this.userManager.createUser(this.theUser);
        verify(this.transactionalManager).saveUser(this.theUser);
        verify(this.transactionalManager).saveUserRole(any(TheUserRoles.class));
    }

    @Test
    public void testInvalidIdThrowsExpectedExceptionAndThusReturnsNull() {
        assertNull(this.userManager.getUserFromNickname(null));
    }

    @Test
    public void testUserUpdateSavesUserAndUpdatesAllData() {
        TheUser theNewestUser = this.theUser;
        String theNewPass = "abrandnewduckingpassword";
        theNewestUser.setUserPassword(theNewPass);
        this.userManager.updateUser(theNewestUser);
        //this.theUser will be the user model that will be updated with new password etc.
        verify(this.transactionalManager).saveUser(this.theUser);
    }

    @Test
    public void testUserConfirmationWithInvalidUserReturnsInvalidUUIDResponse() throws Exception {
        String nickname = "nick";
        when(this.transactionalManager.getUserFromNickname(nickname)).thenReturn(null);
        TheResponse theResponse = this.userManager.confirmUser("uuid", nickname);
        org.junit.Assert.assertEquals("Expected the invalid uuid response", 78, theResponse.getCode());
    }

    @Test
    public void testUserConfirmationWithInvalidUUIDReturnsInvalidUUIDResponse() throws Exception {
        String uuid = "123465798";
        String nickna = "iam";
        TheUser user = UserBuilderManager.aPendingUserWithNickname(nickna).withUUID(uuid).build();
        when(this.transactionalManager.getUserFromNickname(nickna)).thenReturn(user);
        TheResponse theResponse = this.userManager.confirmUser("invalidUUID", nickna);
        org.junit.Assert.assertEquals("Expected the invalid uuid response", 78, theResponse.getCode());
    }

    @Test
    public void testUserConfirmationWithValidUUIDReturnsOKResponse() throws Exception {
        String uuid = "123465798";
        String nickna = "iam";
        TheUser user = UserBuilderManager.aPendingUserWithNickname(nickna).withUUID(uuid).build();
        when(this.transactionalManager.getUserFromNickname(nickna)).thenReturn(user);
        TheResponse theResponse = this.userManager.confirmUser(uuid, nickna);
        org.junit.Assert.assertEquals("Expected the valid uuid response", 0, theResponse.getCode());
    }
}

