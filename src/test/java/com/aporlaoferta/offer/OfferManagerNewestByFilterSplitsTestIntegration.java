package com.aporlaoferta.offer;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferCategory;
import com.aporlaoferta.model.OfferFilters;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.TransactionalManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:aporlaoferta-inmemory-test-context.xml",
        "classpath:aporlaoferta-managers-test-context.xml"})
public class OfferManagerNewestByFilterSplitsTestIntegration {

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
    public void testHundredOffersWithGivenFiltersAreReturned() {
        addOneThousandDummyRandomCategoryOffers();
        OfferFilters offerFilters = createExampleFilter();
        List<TheOffer> theFiltered100CategoryOffers = this.offerManager.getFilteredNextHundredResults(offerFilters);
        assertThat("Expected many offers as filter result", theFiltered100CategoryOffers.size(), greaterThan(0));
        assertAllSelectedOffersContainSameFilters(offerFilters, theFiltered100CategoryOffers);
    }

    private void assertAllSelectedOffersContainSameFilters(OfferFilters offerFilters,
                                                           List<TheOffer> theFiltered100CategoryOffers) {
        int expiredCounter = 0;
        int nonExpiredCounter = 0;
        for (TheOffer theOffer : theFiltered100CategoryOffers) {
            assertThat("Expected offer to be same category",
                    theOffer.getOfferCategory().name(),
                    is(offerFilters.getCategory()));
            assertThat("Expected offer to contain text like title or description",
                    theOffer.getOfferCategory().name(),
                    is(offerFilters.getCategory()));
            int counter = theOffer.isOfferExpired() ? expiredCounter++ : nonExpiredCounter++;
        }
        if (offerFilters.isExpired()) {
            Assert.assertTrue("Expected offers to be expired and not expired",
                    expiredCounter > 0 && expiredCounter > 0);
        }else{

        }

    }

    private OfferFilters createExampleFilter() {
        OfferFilters offerFilters = new OfferFilters();
        offerFilters.setExpired(true);
        offerFilters.setCategory(OfferCategory.ELECTRONICS);
        offerFilters.setText("tigre");
        return offerFilters;
    }

    private void addOneThousandDummyRandomCategoryOffers() {
        for (int i = 0; i < 1000; i++) {
            addRandomCategoryTextAndExpiredOffer();
        }
    }

    private void addRandomCategoryTextAndExpiredOffer() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId()
                .withCategory(selectRandomCategory())
                .withTitle(selectRandomTitle())
                .withDescription(selectRandomTitle())
                .isExpired(selectRandomExpired())
                .build();
        this.theUser.addOffer(anotherOffer);
        this.offerManager.createOffer(anotherOffer);
    }

    private String selectRandomTitle() {
        List<String> offerTitle = Arrays.asList(
                "tres tistres tigres", "tigre happy bullsh",
                "3 muthatristre tistre thistre happyness",
                "more than 3 ti"
        );
        return (String) getRandomFromArray(offerTitle);
    }

    private Boolean selectRandomExpired() {
        List<Boolean> offerExpired = Arrays.asList(
                true, false
        );
        return (Boolean) getRandomFromArray(offerExpired);
    }

    private OfferCategory selectRandomCategory() {
        List<OfferCategory> offerCategory = Arrays.asList(
                OfferCategory.ELECTRONICS,
                OfferCategory.FASHION,
                OfferCategory.HOME,
                OfferCategory.RESTAURANTS,
                OfferCategory.TRAVEL,
                OfferCategory.OTHER
        );
        return (OfferCategory) getRandomFromArray(offerCategory);
    }

    private Object getRandomFromArray(List list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
