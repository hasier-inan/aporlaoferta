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
        TheOffer offer = createCorrectOffer();
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testTitleMaximumLengthIs250() throws Exception {
        TheOffer offer = createCorrectOffer();
        offer.setOfferTitle(createTooLongString(251));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
        offer.setOfferTitle(createTooLongString(250));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testDescriptionMaximumLengthIs2000() throws Exception {
        TheOffer offer = createCorrectOffer();
        offer.setOfferDescription(createTooLongString(2001));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
        offer.setOfferDescription(createTooLongString(2000));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testLinkMaximumLengthIs2000() throws Exception {
        TheOffer offer = createCorrectOffer();
        offer.setOfferLink(createTooLongString(2001));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
        offer.setOfferLink(createTooLongString(2000));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testOriginalPricedMaxIs999999() throws Exception {
        TheOffer offer = createCorrectOffer();
        offer.setOriginalPrice(new BigDecimal(1000000));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
        offer.setOriginalPrice(new BigDecimal(999999));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testOriginalPricedMinIs0() throws Exception {
        TheOffer offer = createCorrectOffer();
        offer.setOriginalPrice(new BigDecimal(-1));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
        offer.setOriginalPrice(new BigDecimal(0));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testFinalPricedMaxIs999999() throws Exception {
        TheOffer offer = createCorrectOffer();
        offer.setFinalPrice(new BigDecimal(1000000));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
        offer.setFinalPrice(new BigDecimal(999999));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    @Test
    public void testFinalPricedMinIs0() throws Exception {
        TheOffer offer = createCorrectOffer();
        offer.setFinalPrice(new BigDecimal(-1));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
        offer.setFinalPrice(new BigDecimal(0));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offer, this.theOfferValidator).hasErrors());
    }

    private String createTooLongString(int numb) {
        return (numb > 0) ? "a" + createTooLongString(--numb) : "";
    }

    private TheOffer createCorrectOffer() {
        TheOffer offer = new TheOffer();
        offer.setOfferCompany(CompanyBuilderManager.aRegularCompanyWithName("d").build());
        offer.setOfferUser(UserBuilderManager.aRegularUserWithNickname("ds").build());
        offer.setOfferCategory(OfferCategory.ELECTRONICA);
        offer.setOfferTitle("sdfjl");
        offer.setOfferImage("linkdsd");
        offer.setOfferLink("dsfgad");
        offer.setFinalPrice(new BigDecimal(122));
        offer.setOriginalPrice(new BigDecimal(122));
        offer.setOfferCreatedDate(new Date());
        offer.setOfferDescription("dsfasdf");
        return offer;
    }
}
