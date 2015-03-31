package com.aporlaoferta.model.validators;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.TheOffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hasiermetal on 29/01/15.
 */
public class TheOfferValidatorTest {

    TheOfferValidator theOfferValidator;
    TheOffer theOffer;

    @Before
    public void setUp() {
        this.theOfferValidator = new TheOfferValidator();
        this.theOffer = OfferBuilderManager.aBasicOfferWithId(1L).build();
    }

    @Test
    public void testTitleIsMandatory() {
        this.theOffer.setOfferTitle(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testFinalPriceIsMandatory() {
        this.theOffer.setOfferTitle(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testImageIsMandatory() {
        this.theOffer.setOfferImage(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testDescriptionIsMandatory() {
        this.theOffer.setOfferDescription(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testLinkIsMandatory() {
        this.theOffer.setOfferLink(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testDateIsMandatory() {
        this.theOffer.setOfferCreatedDate(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testCompanyIsMandatory() {
        this.theOffer.setOfferCompany(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testCategoryIsMandatory() {
        this.theOffer.setOfferCategory(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testUserIsMandatory() {
        this.theOffer.setOfferUser(null);
        Assert.assertTrue(ValidatorHelper.getValidationErrors(this.theOffer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testOfferWithMinimumFieldsThrowNoErrors() {
        TheOffer offer = new TheOffer();
        offer.setOfferCompany(CompanyBuilderManager.aRegularCompanyWithName("d").build());
        offer.setOfferUser(UserBuilderManager.aRegularUserWithNickname("ds").build());
        offer.setOfferCategory(OfferCategory.ELECTRONICS);
        offer.setOfferTitle("sdfjl");
        offer.setOfferImage("linkdsd");
        offer.setOfferLink("dsfgad");
        offer.setFinalPrice(new BigDecimal(122));
        offer.setOfferCreatedDate(new Date());
        offer.setOfferDescription("dsfasdf");
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }
}
