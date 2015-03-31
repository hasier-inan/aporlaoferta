package com.aporlaoferta.model.validators;

import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheUser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by hasiermetal on 29/01/15.
 */
public class TheUserValidatorTest {

    private TheUserValidator theUserValidator;
    private TheUser theUser;

    @Before
    public void setUp() {
        this.theUser = UserBuilderManager.aRegularUserWithNickname("sdf").build();
        this.theUserValidator = new TheUserValidator();
    }

    @Test
    public void testNicknameIsMandatory() {
        this.theUser.setUserNickname("");
        assertTrue(ValidatorHelper.getValidationErrors(this.theUser, this.theUserValidator).hasErrors());
    }

    @Test
    public void testPwdIsMandatory() {
        this.theUser.setUserPassword("");
        assertTrue(ValidatorHelper.getValidationErrors(this.theUser, this.theUserValidator).hasErrors());
    }

    @Test
    public void testEmailIsMandatory() {
        this.theUser.setUserEmail("");
        assertTrue(ValidatorHelper.getValidationErrors(this.theUser, this.theUserValidator).hasErrors());
    }

    @Test
    public void testMinimumParametersReturnNoErrors() {
        TheUser user = new TheUser();
        user.setUserEmail("sdf");
        user.setUserPassword("sdfasdf");
        user.setUserNickname("dsafdf");
        assertFalse(ValidatorHelper.getValidationErrors(this.theUser, this.theUserValidator).hasErrors());
    }
}
