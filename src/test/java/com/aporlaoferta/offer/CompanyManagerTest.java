package com.aporlaoferta.offer;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompanyManagerTest {

    private static final Long THE_ID = 1L;

    @InjectMocks
    CompanyManager companyManager;

    @Mock
    TransactionalManager transactionalManager;

    OfferCompany offerCompany;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.offerCompany = CompanyBuilderManager.aRegularCompanyWithId(THE_ID).build();
        Mockito.when(this.transactionalManager.getCompanyFromId(THE_ID)).thenReturn(offerCompany);
    }

    @Test
    public void testCompanyDaoFindsOneUniqueCompanyFromId() {
        assertNotNull(this.companyManager.getCompanyFromId(THE_ID));
        verify(this.transactionalManager).getCompanyFromId(THE_ID);
    }

    @Test
    public void testCompanyIsCreatedUsingDaos() {
        this.companyManager.createCompany(this.offerCompany);
        verify(this.transactionalManager).saveCompany(this.offerCompany);
    }

    @Test
    public void testAllCompaniesAreObtainedUsingCustomQuery() {
        this.companyManager.getAllCompanies();
        verify(this.transactionalManager).getAllCompanies();
    }

    @Test
    public void testInvalidIdThrowsExpectedExceptionAndThusReturnsNull() {
        assertNull(this.companyManager.getCompanyFromId(null));
    }

    @Test
    public void testNonWatermarkedCompanyReturnsEmptyName() throws Exception {
        Mockito.when(this.transactionalManager.getAllCompanies())
                .thenReturn(Arrays.asList(new OfferCompany[]{this.offerCompany}));
        Assert.assertTrue("Expected empty name",
                isEmpty(this.companyManager.getWatermarkedCompany("http://www.namewatermark.rk/jkdjdskj")));
    }

    @Test
    public void testWatermarkedCompanyIsReturnedWhenNamePartialiMatches() throws Exception {
        String watermarks = "namewatermark urlwaterma.rk partialwatermark";
        String companyName = "the_company";
        OfferCompany watermarkedCompany = CompanyBuilderManager.aRegularCompanyWithName(companyName)
                .withWatermarks(watermarks)
                .build();
        Mockito.when(this.transactionalManager.getAllCompanies())
                .thenReturn(Arrays.asList(new OfferCompany[]{watermarkedCompany}));
        Assert.assertThat("Expected watermarked company name",
                this.companyManager.getWatermarkedCompany("http://www.namewatermark.rk/jkdjdskj"),
                is(companyName));
    }
}

