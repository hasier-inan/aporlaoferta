package com.aporlaoferta.offer;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.DateRange;
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
public class OfferManagerNewestSplitsTestIntegration {

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
    public void testHundredOfOffersAreReturned() {
        addOneThousandDummyOffers();
        assertThat("Expected 1000 offers persisted in the db", this.offerManager.countAllOffers(), is(1000L));
        List<TheOffer> theNewest100Offers = this.offerManager.getNextHundredOffers(0L, DateRange.ALL);
        assertThat("Expected the newest offer id to be 1000", theNewest100Offers.get(0).getId(), is(1000L));
        List<TheOffer> theNext100Offers = this.offerManager.getNextHundredOffers(125L, DateRange.ALL);
        assertThat("Expected the after 125 offers, newest offer id to be 875", theNext100Offers.get(0).getId(), is(875L));
    }

    private void addOneThousandDummyOffers() {
        for (int i = 0; i < 1000; i++) {
            addOfferToDB();
        }
    }

    private void addOfferToDB() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        this.theUser.addOffer(anotherOffer);
        this.offerManager.createOffer(anotherOffer);
    }

}

