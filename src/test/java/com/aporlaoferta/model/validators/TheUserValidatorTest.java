package com.aporlaoferta.model.validators;

import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.TheUser;
import org.junit.Assert;
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

    @Test
    public void testMaximumLengthForUsernameIs250Chars() throws Exception {
        TheUser user = createCorrectUser();
        user.setUserNickname(createTooLongString(251));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(user, this.theUserValidator).hasErrors());
        user.setUserNickname(createTooLongString(250));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(user, this.theUserValidator).hasErrors());
    }

    @Test
    public void testMaximumLengthForPasswordIs250Chars() throws Exception {
        TheUser user = createCorrectUser();
        user.setUserPassword(createTooLongString(251));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(user, this.theUserValidator).hasErrors());
        user.setUserPassword(createTooLongString(250));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(user, this.theUserValidator).hasErrors());
    }

    @Test
    public void testMaximumLengthForEmailIs250Chars() throws Exception {
        TheUser user = createCorrectUser();
        user.setUserEmail(createTooLongString(251)+"@aa.com");
        Assert.assertTrue(ValidatorHelper.getValidationErrors(user, this.theUserValidator).hasErrors());
        user.setUserEmail(createTooLongString(243)+"@aa.com");
        Assert.assertFalse(ValidatorHelper.getValidationErrors(user, this.theUserValidator).hasErrors());
    }

    private TheUser createCorrectUser() {
        return UserBuilderManager.aRegularUserWithNickname("sad").build();

    }

    private String createTooLongString(int numb) {
        return (numb > 0) ? "a" + createTooLongString(--numb) : "";
    }
}
