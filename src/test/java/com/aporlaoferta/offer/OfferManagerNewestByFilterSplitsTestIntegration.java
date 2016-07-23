package com.aporlaoferta.offer;

import com.aporlaoferta.data.FilterBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.DateRange;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
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

    private static Random RANDOM_GENERATOR = new Random();
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
        List<TheOffer> theFiltered100CategoryOffers = this.offerManager.getFilteredNextHundredResults(offerFilters, 0L);
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
                    is(offerFilters.getSelectedcategory()));
            assertThat("Expected offer to contain text like title or description",
                    theOffer.getOfferCategory().name(),
                    is(offerFilters.getSelectedcategory()));
            Assert.assertTrue("Expected offer to have been created within the last week",
                    theOffer.getOfferCreatedDate().compareTo(createOneWeekAgoDate()) == 1);
            int counter = theOffer.isOfferExpired() ? expiredCounter++ : nonExpiredCounter++;
        }
        if (offerFilters.isExpired()) {
            Assert.assertTrue("Expected offers to be expired and not expired",
                    expiredCounter > 0 && nonExpiredCounter > 0);
        } else {
            Assert.assertTrue("Expected offers to be expired and not expired",
                    expiredCounter == 0 && nonExpiredCounter > 0);
        }
    }

    private OfferFilters createExampleFilter() {
        return FilterBuilderManager.anAllElectronicsFilterWithText("tigre").withDateRange(DateRange.WEEK).build();
    }

    private void addOneThousandDummyRandomCategoryOffers() {
        for (int i = 0; i < 1000; i++) {
            addRandomCategoryTextAndExpiredOffer();
        }
    }

    private void addRandomCategoryTextAndExpiredOffer() {
        TheOffer anotherOffer = OfferBuilderManager.aBasicOfferWithoutId()
                .withCategory(selectRandomCategory())
                .withTitle(String.format("%s", selectRandomTitle() + Math.random()))
                .withDescription(selectRandomTitle())
                .withPositives(Long.valueOf(RANDOM_GENERATOR.nextInt(1000)))
                .withNegatives(Long.valueOf(RANDOM_GENERATOR.nextInt(1000)))
                .createdOn(createRandomDate())
                .isExpired(selectRandomExpired())
                .build();
        this.theUser.addOffer(anotherOffer);
        this.offerManager.createOffer(anotherOffer);
    }

    private Date createRandomDate() {
        Calendar calendar = Calendar.getInstance();
        Random rnd = new Random();
        int days = rnd.nextInt(31);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    private Date createOneWeekAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
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
                OfferCategory.ELECTRONICA,
                OfferCategory.MODA,
                OfferCategory.HOGAR,
                OfferCategory.RESTAURANTES,
                OfferCategory.VIAJES,
                OfferCategory.OTROS
        );
        return (OfferCategory) getRandomFromArray(offerCategory);
    }

    private Object getRandomFromArray(List list) {
        return list.get((int) (Math.random() * list.size()));
    }
}
