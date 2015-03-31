package com.aporlaoferta.offer;

import com.aporlaoferta.dao.CompanyDAO;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
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
import static org.mockito.Mockito.verify;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompanyManagerTest {

    private static final Long THE_ID = 1L;
    @InjectMocks
    CompanyManager companyManager;

    @Mock
    CompanyDAO companyDAO;

    OfferCompany offerCompany;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.offerCompany = CompanyBuilderManager.aRegularCompanyWithId(THE_ID).build();
        Mockito.when(this.companyDAO.findOne(THE_ID)).thenReturn(offerCompany);
    }

    @Test
    public void testCompanyDaoFindsOneUniqueCompanyFromId() {
        assertNotNull(this.companyManager.getCompanyFromId(THE_ID));
        verify(this.companyDAO).findOne(THE_ID);
    }

    @Test
    public void testCompanyIsCreatedUsingDaos() {
        this.companyManager.createCompany(this.offerCompany);
        verify(this.companyDAO).save(this.offerCompany);
    }

    @Test
    public void testAllCompaniesAreObtainedUsingCustomQuery() {
        this.companyManager.getAllCompanies();
        verify(this.companyDAO).getListOfPersistedCompanies();
    }

    @Test
    public void testInvalidIdThrowsExpectedExceptionAndThusReturnsNull() {
        assertNull(this.companyManager.getCompanyFromId(null));
    }

}

