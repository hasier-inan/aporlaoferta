package com.aporlaoferta.model.validators;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by hasiermetal on 29/01/15.
 */
public class OfferCompanyValidatorTest {

    OfferCompanyValidator offerCompanyValidator;
    OfferCompany offerCompany;

    @Before
    public void setUp() {
        this.offerCompany = CompanyBuilderManager.aRegularCompanyWithName("djfkhasd").build();
        this.offerCompanyValidator = new OfferCompanyValidator();
    }

    @Test
    public void testCompanyNameIsMandatory() {
        this.offerCompany.setCompanyName(null);
        assertTrue(ValidatorHelper.getValidationErrors(this.offerCompany, this.offerCompanyValidator).hasErrors());
    }

    @Ignore //no longer mandatory
    @Test
    public void testCompanyUrlIsMandatory() {
        this.offerCompany.setCompanyUrl(null);
        assertTrue(ValidatorHelper.getValidationErrors(this.offerCompany, this.offerCompanyValidator).hasErrors());
    }

    @Test
    public void testMinimumParametersThrowNoException() {
        OfferCompany company = new OfferCompany();
        company.setCompanyName("nam");
        company.setCompanyUrl("fjdsklj");
        assertFalse(ValidatorHelper.getValidationErrors(company, this.offerCompanyValidator).hasErrors());
    }

    @Test
    public void testCompanyNameMaxIs250() throws Exception {
        OfferCompany offerCompany = CompanyBuilderManager.aRegularCompanyWithName("a").build();
        offerCompany.setCompanyName(createTooLongString(251));
        Assert.assertTrue(ValidatorHelper.getValidationErrors(offerCompany, this.offerCompanyValidator).hasErrors());
        offerCompany.setCompanyName(createTooLongString(250));
        Assert.assertFalse(ValidatorHelper.getValidationErrors(offerCompany, this.offerCompanyValidator).hasErrors());
    }

    private String createTooLongString(int numb) {
        return (numb > 0) ? "a" + createTooLongString(--numb) : "";
    }
}
