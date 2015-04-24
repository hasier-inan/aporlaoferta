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

import static org.hamcrest.Matchers.greaterThan;
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
public class OfferManagerTestIntegration {

    @Autowired
    TransactionalManager transactionalManager;

    @Autowired
    private OfferManager offerManager;

    private TheOffer theMainOffer;

    @Before
    public void setUp() {
        theMainOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        this.transactionalManager.saveOffer(theMainOffer);
    }

    @Test
    public void testAtLeastOneOfferHasBeenPersistedInDatabase() {
        assertThat("Expected only one offer to be in the db", this.offerManager.countAllOffers(), greaterThan(1L));
    }

    @Test
    public void testOfferIsObtainedFromDB() {
        assertThat("Expected only one offer to be in the db", this.offerManager.countAllOffers(), is(1L));
        TheOffer theOffer = this.offerManager.getOfferFromId(1L);
        assertNotNull(theOffer);
        assertThat("Expected the offer id to be 1", theOffer.getId(), is(1L));
    }

    @Test
    public void testOfferCanBeExpired() {
        TheOffer theOffer = this.offerManager.getOfferFromId(1L);
        assertFalse("Expected the offer to be active", theOffer.isOfferExpired());
        TheOffer theOfferExpired = this.offerManager.expireOffer(theOffer);
        assertTrue("Expected the offer to be expired", theOfferExpired.isOfferExpired());

    }

}

