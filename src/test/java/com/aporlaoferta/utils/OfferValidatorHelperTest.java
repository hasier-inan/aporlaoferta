package com.aporlaoferta.utils;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by hasiermetal on 22/01/15.
 */
public class OfferValidatorHelperTest {

    private OfferValidatorHelper offerValidatorHelper;

    @Before
    public void setUp() {
        this.offerValidatorHelper = new OfferValidatorHelper(new OfferCompanyValidator(), new TheUserValidator(), new TheOfferValidator(), new OfferCommentValidator(), new OfferCommentQuoteValidator());
    }

    @Test
    public void testCompanyValidationThrowsNoExceptionWhenMinimumParametersAreIncluded() {
        this.offerValidatorHelper.validateCompany(CompanyBuilderManager.aRegularCompanyWithName("ducku").build());
    }

    @Test(expected = ValidationException.class)
    public void testValidationExceptionIsReceivedWhenCompanyHasNotGotMinimumParameters() {
        this.offerValidatorHelper.validateCompany(new OfferCompany());
    }

    @Test
    public void testOfferValidationThrowsNoExceptionWhenMinimumParametersAreIncluded() {
        this.offerValidatorHelper.validateOffer(OfferBuilderManager.aBasicOfferWithId(1L).build());
    }

    @Test(expected = ValidationException.class)
    public void testValidationExceptionIsReceivedWhenOfferHasNotGotMinimumParameters() {
        this.offerValidatorHelper.validateOffer(new TheOffer());
    }

    @Test
    public void testUserValidationThrowsNoExceptionWhenMinimumParametersAreIncluded() {
        this.offerValidatorHelper.validateUser(UserBuilderManager.aRegularUserWithNickname("saedf").build());
    }

    @Test(expected = ValidationException.class)
    public void testValidationExceptionIsReceivedWhenUserHasNotGotMinimumParameters() {
        this.offerValidatorHelper.validateUser(new TheUser());
    }

    @Test
    public void testCommentValidationThrowsNoExceptionWhenMinimumParametersAreIncluded() {
        this.offerValidatorHelper.validateComment(CommentBuilderManager.aBasicCommentWithId(1L).build());
    }

    @Test(expected = ValidationException.class)
    public void testValidationExceptionIsReceivedWhenCommentHasNotGotMinimumParameters() {
        this.offerValidatorHelper.validateComment(new OfferComment());
    }

    @Test
    public void testQuoteValidationThrowsNoExceptionWhenMinimumParametersAreIncluded() {
        this.offerValidatorHelper.validateQuote(CommentBuilderManager.aCommentWithQuotedCommentAndId(CommentBuilderManager.aBasicCommentWithId(1L).build(), 2L).build());
    }

    @Test(expected = ValidationException.class)
    public void testValidationExceptionIsReceivedWhenQuoteHasNotGotMinimumParameters() {
        this.offerValidatorHelper.validateQuote(new OfferComment());
    }
}
