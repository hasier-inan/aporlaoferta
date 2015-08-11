package com.aporlaoferta.offer;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferCategory;
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

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
public class OfferManagerNewestByCategorySplitsTestIntegration {

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
    public void testHundredOffersFromSelectedCategoryAreReturned() {
        OfferCategory offerCategory = OfferCategory.ELECTRONICA;
        addOneThousandDummyRandomCategoryOffers();
        assertThat("Expected 1000 offers persisted in the db", this.offerManager.countAllOffers(), is(1000L));
        List<TheOffer> theNewest100CategoryOffers = this.offerManager.getNextHundredOffersByCategory(0L, offerCategory);
        assertAllSelectedOffersAreFromTheSameCategory(offerCategory, theNewest100CategoryOffers);
    }

    private void assertAllSelectedOffersAreFromTheSameCategory(OfferCategory offerCategory, List<TheOffer> theNewest100CategoryOffers) {
        for (TheOffer theOffer : theNewest100CategoryOffers) {
            assertThat("Expected offer to be same category", theOffer.getOfferCategory(), is(offerCategory));
        }
    }

    private void addOneThousandDummyRandomCategoryOffers() {
        for (int i = 0; i < 1000; i++) {
            addRandomCategoryOffer();
        }
    }

    private void addRandomCategoryOffer() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId()
                .withCategory(selectRandomCategory())
                .build();
        this.theUser.addOffer(anotherOffer);
        this.offerManager.createOffer(anotherOffer);
    }

    private OfferCategory selectRandomCategory() {
        List<OfferCategory> offerCategory = Arrays.asList(
                OfferCategory.ELECTRONICA,
                OfferCategory.MODA,
                OfferCategory.HOGAR,
                OfferCategory.RESTAURANTES,
                OfferCategory.VIAJES,
                OfferCategory.OTROS
        );
        return offerCategory.get((int)(Math.random()*offerCategory.size()));
    }

}

