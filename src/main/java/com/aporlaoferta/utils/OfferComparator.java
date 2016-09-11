package com.aporlaoferta.utils;

import com.aporlaoferta.model.TheOffer;

import java.util.Comparator;

/**
 * Created by hasiermetal on 2/03/14.
 */
public final class OfferComparator implements Comparator<TheOffer> {

    @Override
    public int compare(TheOffer requestOne, TheOffer requestTwo) {
        return requestTwo.getOfferCreatedDate().compareTo(requestOne.getOfferCreatedDate());
    }

}
