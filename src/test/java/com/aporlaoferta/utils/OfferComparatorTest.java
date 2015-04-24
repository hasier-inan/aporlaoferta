package com.aporlaoferta.utils;

import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.model.TheOffer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Created by hasiermetal on 22/01/15.
 */
public class OfferComparatorTest {

    TheOffer eldestOffer;
    TheOffer medOffer;
    TheOffer newestOffer;

    @Before
    public void setUp() {
        this.eldestOffer = OfferBuilderManager.aBasicOfferWithId(1L).build();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2015, 01, 05);
        this.eldestOffer.setOfferCreatedDate(calendar1.getTime());

        this.medOffer = OfferBuilderManager.aBasicOfferWithId(2L).build();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2015, 01, 06);
        this.medOffer.setOfferCreatedDate(calendar2.getTime());

        this.newestOffer = OfferBuilderManager.aBasicOfferWithId(3L).build();
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2015, 01, 07);
        this.newestOffer.setOfferCreatedDate(calendar3.getTime());
    }

    @Test
    public void testOffersAreSortedByDateFromNewToOld() {
        List<TheOffer> theOffers = Arrays.asList(this.medOffer, this.eldestOffer, this.newestOffer);
        Collections.sort(theOffers, new OfferComparator());
        assertThat(theOffers, contains(this.newestOffer, this.medOffer, this.eldestOffer));
    }


}
