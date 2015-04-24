package com.aporlaoferta.utils;

import com.aporlaoferta.model.TheOffer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hasiermetal on 2/03/14.
 */
public final class OfferComparator implements Comparator<TheOffer> {

    @Override
    public int compare(TheOffer requestOne, TheOffer requestTwo) {
        return requestTwo.getOfferCreatedDate().compareTo(requestOne.getOfferCreatedDate());
    }

}
