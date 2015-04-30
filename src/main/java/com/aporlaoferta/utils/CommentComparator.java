package com.aporlaoferta.utils;

import com.aporlaoferta.model.OfferComment;

import java.util.Comparator;

/**
 * Created by hasiermetal on 2/03/14.
 */
public final class CommentComparator implements Comparator<OfferComment> {

    @Override
    public int compare(OfferComment requestOne, OfferComment requestTwo) {
        return requestTwo.getCommentCreationDate().compareTo(requestOne.getCommentCreationDate());
    }

}
