package com.aporlaoferta.controller;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;

/**
 * Created by hasiermetal on 29/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompanyControllerTest {

    @InjectMocks
    CompanyController companyController;
    @Mock
    OfferValidatorHelper offerValidatorHelper;
    @Mock
    CompanyManager companyManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCommentWithRawCommentReturnsExpectedCodeIfValidationExceptionOccurred() {
        doThrow(new ValidationException("error")).when(this.offerValidatorHelper)
                .validateCompany((OfferCompany) anyObject());
        TheResponse result = this.companyController.createCompany(new OfferCompany());
        assertTrue(result.getResponseResult() == ResultCode.CREATE_COMPANY_VALIDATION_ERROR.getResponseResult());
        assertEquals(result.getDescription(), "Validation process failed while creating company");
    }

    @Test
    public void testSuccessfullyCreatedCompanyReturnsProperCode() {
        long id = 2L;
        OfferCompany offerCompany = CompanyBuilderManager.aRegularCompanyWithId(id).build();
        Mockito.when(this.companyManager.createCompany(any(OfferCompany.class))).thenReturn(offerCompany);
        TheResponse result = this.companyController.createCompany(offerCompany);
        assertTrue(ResultCode.ALL_OK.getCode() == result.getCode());
        assertEquals(result.getDescription(), "Company successfully created. Id: " + id);
    }

    @Test
    public void testEmptyObjectMessageIsReturnedIfCantUpdateTheDB() {
        Mockito.when(this.companyManager.createCompany(any(OfferCompany.class))).thenReturn(null);
        TheResponse result = this.companyController.createCompany(
                CompanyBuilderManager.aRegularCompanyWithName("sdaf").build());
        assertTrue(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getCode() == result.getCode());
        assertEquals(result.getDescription(), ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getResultDescription());
    }

    @Test
    public void testListOfCompaniesAreCorrectlyProcessedAndListIsReturnedAsResult() {
        ArrayList<OfferCompany> value = new ArrayList<OfferCompany>();
        value.add(CompanyBuilderManager.aRegularCompanyWithId(2L).build());
        Mockito.when(this.companyManager.getAllCompanies()).thenReturn(value);
        List<OfferCompany> allCompanies = this.companyController.getCompanies();
        assertTrue(allCompanies.equals(value));
    }

    @Test
    public void testEmptyListIsReturnedIfNoCompaniesAreFound() {
        Mockito.when(this.companyManager.getAllCompanies()).thenReturn(null);
        List<OfferCompany> allCompanies = this.companyController.getCompanies();
        assertNotNull(allCompanies);
        assertTrue(allCompanies.size() == 0);
    }
}
