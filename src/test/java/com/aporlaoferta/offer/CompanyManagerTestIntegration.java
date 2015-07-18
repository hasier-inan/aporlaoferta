package com.aporlaoferta.offer;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.TransactionalManager;
import com.aporlaoferta.utils.UrlParser;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
@Transactional
public class CompanyManagerTestIntegration {

    protected static final String COMPANY_NAME = "This is the duck factory";

    @Autowired
    protected TransactionalManager transactionalManager;

    @Autowired
    protected CompanyManager companyManager;

    @Autowired
    private UrlParser urlParser;

    protected OfferCompany offerCompany;

    @Before
    public void setUp() {
        offerCompany = CompanyBuilderManager.aRegularCompanyWithName(COMPANY_NAME).build();
        this.companyManager.createCompany(offerCompany);
    }

    @Test
    public void testAtLeastOneCompanyHasBeenPersistedInDatabase() {
        assertThat("Expected only one company to be in the db", this.transactionalManager.getAllCompanies().size(), greaterThan(0));
        testOfferIsObtainedFromDB();
    }

    public void testOfferIsObtainedFromDB() {
        Long persistedId = this.offerCompany.getId();
        OfferCompany company = this.companyManager.getCompanyFromId(persistedId);
        assertNotNull(company);
        assertThat("Expected the offer id to be 1", company.getId(), is(persistedId));
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

    @Test
    public void testFinalRawLinkIsObtainedFromShortenerUrl() throws Exception {
        String theHiddenLink = "http://www.amazon.es/gp/product/B00VIA4N6S?&th=1";
        String theShortenerUrl = "http://goo.gl/MnQf11";
        Assert.assertEquals(theHiddenLink, this.urlParser.obtainFinalEndpoint(theShortenerUrl));
    }
}

