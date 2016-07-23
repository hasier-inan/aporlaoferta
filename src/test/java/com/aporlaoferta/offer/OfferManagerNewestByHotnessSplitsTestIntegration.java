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
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
public class OfferManagerNewestByHotnessSplitsTestIntegration {

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
    public void testHundredOffersAreReturnedSortedByHotness() {
        addOneThousandDummyRandomHotnessOffers();
        assertThat("Expected 1000 offers persisted in the db", this.offerManager.countAllOffers(), is(1000L));
        List<TheOffer> theHottest100Offers = this.offerManager.getNextHundredHottestOffers(0L, DateRange.ALL);
        assertAllSelectedOffersAreSortedByHotness(theHottest100Offers);
    }

    private void assertAllSelectedOffersAreSortedByHotness(List<TheOffer> hottestOffers) {
        Long lastHottestDeal = 500L;
        for (TheOffer theOffer : hottestOffers) {
            Long howHot = theOffer.getOfferPositiveVote() - theOffer.getOfferNegativeVote();
            assertThat("Expected offer to be sorted by hottness", howHot, lessThanOrEqualTo(lastHottestDeal));
            lastHottestDeal = howHot;
        }
    }

    private void addOneThousandDummyRandomHotnessOffers() {
        for (int i = 0; i < 1000; i++) {
            addRandomHotnessOffer();
        }
    }

    private void addRandomHotnessOffer() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId()
                .withPositives(selectRandomFeedback(499))
                .withNegatives(selectRandomFeedback(100))
                .build();
        this.theUser.addOffer(anotherOffer);
        this.offerManager.createOffer(anotherOffer);
    }

    private Long selectRandomFeedback(double maximumPositives) {
        int random = (int) (Math.random() * maximumPositives);
        return new Long(random);
    }

}

