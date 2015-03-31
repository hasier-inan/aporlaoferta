package com.aporlaoferta.offer;

import com.aporlaoferta.dao.OfferDAO;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.TheOffer;
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
public class OfferManagerTestIntegration {

    @Autowired
    OfferDAO offerDAO;

    @Autowired
    private OfferManager offerManager;

    private TheOffer theMainOffer;

    @Before
    public void setUp() {
        theMainOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        this.offerDAO.save(theMainOffer);
    }

    @Test
    public void testUniqueUserIsInTheInMemoryDatabase() {
        assertThat("Expected only one offer to be in the db", this.offerDAO.count(), is(1L));
    }


    @Test
    public void testHundredOfOffersAreReturned() {
        addOneThousandDummyOffers();
        assertThat("Expected only one offer to be in the db", this.offerDAO.count(), is(1001L));
        List<TheOffer> theNewest100Offers = this.offerManager.getNextHundredOffers(0L);
        assertThat("Expected the newest offer id to be 1001", theNewest100Offers.get(0).getId(), is( 1001L));
        List<TheOffer> theNext100Offers = this.offerManager.getNextHundredOffers(100L);
        assertThat("Expected the next newest offer id to be 901", theNext100Offers.get(0).getId(), is(901L));
    }

    @Test
    public void testOfferIsObtainedFromDB() {
        assertThat("Expected only one offer to be in the db", this.offerDAO.count(), is(1L));
        TheOffer theOffer = this.offerManager.getOfferFromId(1L);
        assertNotNull(theOffer);
        assertThat("Expected the offer id to be 1", theOffer.getId(), is(1L));
        assertThat("Expected same offer company", theOffer.getOfferCompany(), is(this.theMainOffer.getOfferCompany()));
        assertThat("Expected same offer owner", theOffer.getOfferUser(), is(this.theMainOffer.getOfferUser()));
    }

    @Test
    public void testOfferCanBeExpired() {
        TheOffer theOffer = this.offerManager.getOfferFromId(1L);
        assertFalse("Expected the offer to be active", theOffer.isOfferExpired());
        TheOffer theOfferExpired = this.offerManager.expireOffer(theOffer);
        assertTrue("Expected the offer to be expired", theOfferExpired.isOfferExpired());

    }

    private void addOneThousandDummyOffers() {
        for (int i = 0; i < 1000; i++) {
            addOfferToDB();
        }
    }

    private void addOfferToDB() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId().build();
        anotherOffer.setOfferCompany(this.theMainOffer.getOfferCompany());
        anotherOffer.setOfferUser(this.theMainOffer.getOfferUser());
        //TODO: Authentication stuff for logged users only
        this.offerManager.createOffer(anotherOffer);
    }

}

