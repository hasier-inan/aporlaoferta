package com.aporlaoferta.offer;

import com.aporlaoferta.data.CompanyBuilder;
import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.TransactionalManager;
import com.aporlaoferta.utils.UnhealthyException;
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
    public static final String RAW_LINK = "http://www.amazon.es/gp/product/B00VIA4N6S";

    @Autowired
    protected TransactionalManager transactionalManager;

    @Autowired
    protected CompanyManager companyManager;

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
    public void testAffiliationLinkIsCreatedForTheCompany() throws Exception {
        this.offerCompany.setCompanyAffiliateId("3333");
        this.offerCompany.setCompanyName("Amazon");
        String theParsedLink = this.companyManager.createAffiliationLink(this.offerCompany, RAW_LINK);
        assertNotNull(theParsedLink);
        assertThat("expected to find the affiliation id within the parsed link", theParsedLink,
                Matchers.containsString(this.offerCompany.getCompanyAffiliateId()));
    }

    @Test
    public void testNoAffiliationLinkIsCreatedIfNullOfferIsProvided() throws Exception {
        Assert.assertTrue(RAW_LINK.equals(this.companyManager.createAffiliationLink(null, RAW_LINK)));
    }

    @Test
    public void testNoAffiliationLinkIsCreatedIfCompanyHasNoAffiliateId() throws Exception {
        this.offerCompany.setCompanyAffiliateId("");
        Assert.assertTrue(RAW_LINK.equals(this.companyManager.createAffiliationLink(this.offerCompany, RAW_LINK)));
    }

    @Test(expected = UnhealthyException.class)
    public void testNoAffiliationLinkIsCreatedIfNoRawLinkIsProvided() throws Exception {
        this.offerCompany.setCompanyAffiliateId("323232");
        this.companyManager.createAffiliationLink(this.offerCompany, "");
    }

}

