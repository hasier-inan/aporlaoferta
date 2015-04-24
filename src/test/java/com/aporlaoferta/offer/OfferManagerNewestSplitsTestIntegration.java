package com.aporlaoferta.offer;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
public class OfferManagerNewestSplitsTestIntegration {

    @Autowired
    private OfferManager offerManager;

    @Test
    public void testHundredOfOffersAreReturned() {
        addOneThousandDummyOffers();
        assertThat("Expected only one offer to be in the db", this.offerManager.countAllOffers(), is(1000L));
        List<TheOffer> theNewest100Offers = this.offerManager.getNextHundredOffers(0L);
        assertThat("Expected the newest offer id to be 1001", theNewest100Offers.get(0).getId(), is(1000L));
        List<TheOffer> theNext100Offers = this.offerManager.getNextHundredOffers(100L);
        assertThat("Expected the next newest offer id to be 901", theNext100Offers.get(0).getId(), is(900L));
    }

    private void addOneThousandDummyOffers() {
        for (int i = 0; i < 1000; i++) {
            addOfferToDB();
        }
    }

    private void addOfferToDB() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        this.offerManager.createOffer(anotherOffer);
    }

}

