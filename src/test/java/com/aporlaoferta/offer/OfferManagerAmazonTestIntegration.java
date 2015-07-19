package com.aporlaoferta.offer;

import com.aporlaoferta.data.CompanyBuilderManager;
import com.aporlaoferta.data.FilterBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
public class OfferManagerAmazonTestIntegration {

    @Autowired
    private OfferManager offerManager;

    @Autowired
    private TransactionalManager transactionalManager;

    private TheUser theUser;

    @Before
    public void setUp() {
        this.theUser = this.transactionalManager.saveUser(UserBuilderManager.aRegularUserWithNickname(UUID.randomUUID().toString()).build());
    }

    @Test
    public void testAmazonOfferIsCreated() {
        addAmazonOfferToDB();
        List<TheOffer> amazonOffers = this.offerManager.getFilteredNextHundredResults(createAmazonFilter());
        assertThat("Expected the amazon offer in the result set", amazonOffers.get(0).getOfferCompany().getCompanyName(), is("Amazon"));
    }

    private OfferFilters createAmazonFilter() {
        return FilterBuilderManager.anAmazonFilter("amazon").build();
    }

    private OfferCompany createAmazonCompany() {
        return CompanyBuilderManager.aRegularCompanyWithName("Amazon").build();
    }

    private void addAmazonOfferToDB() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId()
                .withCompany(createAmazonCompany())
                .build();
        this.theUser.addOffer(anotherOffer);
        this.offerManager.createOffer(anotherOffer);
    }

}

