package com.aporlaoferta.utils;

import com.aporlaoferta.model.TheOffer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hasiermetal on 2/03/14.
 */
public final class OfferComparator {

    private OfferComparator() {
        super();
    }

    private static Comparator<TheOffer> mostRecentFirst() {
        return new Comparator<TheOffer>() {
            @Override
            public int compare(TheOffer o1, TheOffer o2) {
                return o2.getOfferCreatedDate().compareTo(o1.getOfferCreatedDate());
            }
        };
    }

    public static List<TheOffer> sort(TheOffer... theOffers) {
        List<TheOffer> sortedOffers = Arrays.asList(theOffers);
        Collections.sort(sortedOffers, mostRecentFirst());
        return sortedOffers;
    }

}
