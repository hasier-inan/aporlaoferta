package com.aporlaoferta.offer;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.FilterBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
public class OfferManagerAmazonTestIntegration {

    private static final String AFFILIATE_ID = "aporlaoferta-21";

    @Autowired
    private OfferManager offerManager;

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private TransactionalManager transactionalManager;

    private TheUser theUser;

    @Before
    public void setUp() {
        this.theUser = this.transactionalManager.saveUser(UserBuilderManager.aRegularUserWithNickname(UUID.randomUUID().toString()).build());
    }

    @Test
    public void testAmazonOfferIsCreatedAndAffiliationIdIncludedInUrl() throws Exception{
        addAmazonOfferToDB();
        List<TheOffer> amazonOffers = this.offerManager.getFilteredNextHundredResults(createAmazonFilter());
        TheOffer theAmazonOffer = amazonOffers.get(0);
        assertThat("Expected the amazon offer in the result set", theAmazonOffer.getOfferCompany().getCompanyName(), is("Amazon"));
        assertAffiliationIdIsIncludedInFinalUrl(theAmazonOffer);
    }

    private void assertAffiliationIdIsIncludedInFinalUrl(TheOffer theAmazonOffer) {
        assertThat("Expected amazon affiliation id to be in final Url", theAmazonOffer.getOfferLink(), endsWith(AFFILIATE_ID));
    }

    private OfferFilters createAmazonFilter() {
        return FilterBuilderManager.anAmazonFilter("amazon").build();
    }

    private OfferCompany createAmazonCompany() {

        return CompanyBuilderManager
                .aRegularCompanyWithName("Amazon")
                .withAffiliateId(AFFILIATE_ID)
                .build();
    }

    private void addAmazonOfferToDB() throws Exception {
        OfferCompany amazonCompany = createAmazonCompany();
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId()
                .withCompany(amazonCompany)
                .withLink(this.companyManager.createAffiliationLink(amazonCompany, "http://kjhsdfjksad.ksdf/"))
                .build();
        this.theUser.addOffer(anotherOffer);
        this.offerManager.createOffer(anotherOffer);
    }

}

