package com.aporlaoferta.offer;

import com.aporlaoferta.dao.CompanyDAO;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
@Transactional
public class CompanyManagerTestIntegration {

    private static final String COMPANY_NAME = "This is the duck factory";
    @Autowired
    CompanyDAO companyDAO;

    @Autowired
    private CompanyManager companyManager;

    private OfferCompany offerCompany;

    @Before
    public void setUp() {
        offerCompany = CompanyBuilderManager.aRegularCompanyWithName(COMPANY_NAME).build();
        this.companyManager.createCompany(offerCompany);
    }

    @Test
    public void testUniqueCompanyIsInTheInMemoryDatabase() {
        assertThat("Expected only one company to be in the db", this.companyDAO.count(), is(1L));
    }

    @Test
    public void testOfferIsObtainedFromDB() {
        OfferCompany company = this.companyManager.getCompanyFromId(1L);
        assertNotNull(company);
        assertThat("Expected the offer id to be 1", company.getId(), is(1L));
        assertThat("Expected same offer company", company.getCompanyName(), is(COMPANY_NAME));
    }

    @Test
    public void testAllCompaniesAreObtained() {
        List<OfferCompany> offerCompanies = this.companyManager.getAllCompanies();
        assertThat("Expected one company", offerCompanies.size(), is(1));
        this.companyManager.createCompany(CompanyBuilderManager.aRegularCompanyWithName("sdafsdfsdf").build());
        offerCompanies = this.companyManager.getAllCompanies();
        assertThat("Expected one company", offerCompanies.size(), is(2));
    }

    @Test
    public void testAffiliationLinkIsCreatedForTheCompany() {
        //TODO: Create affiliation tests for each company
        this.offerCompany.setCompanyAffiliateId("3333");
        String theParsedLink = this.companyManager.createAffiliationLink(this.offerCompany, "raw.link");
        assertNotNull(theParsedLink);
        assertThat("expected to find the affiliation id within the parsed link", theParsedLink,
                Matchers.containsString(this.offerCompany.getCompanyAffiliateId()));
    }

    @Test
    public void testNoAffiliationLinkIsCreatedIfNullOfferIsProvided() {
        assertNull(this.companyManager.createAffiliationLink(null, "raw.link"));
    }

    @Test
    public void testNoAffiliationLinkIsCreatedIfCompanyHasNoAffiliateId() {
        this.offerCompany.setCompanyAffiliateId("");
        assertNull(this.companyManager.createAffiliationLink(this.offerCompany, "raw.link"));
    }

    @Test
    public void testNoAffiliationLinkIsCreatedIfNoRawLinkIsProvided() {
        this.offerCompany.setCompanyAffiliateId("323232");
        assertNull(this.companyManager.createAffiliationLink(this.offerCompany, ""));
    }

}

