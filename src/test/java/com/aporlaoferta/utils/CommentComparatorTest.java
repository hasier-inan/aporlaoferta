package com.aporlaoferta.utils;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
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
public class CommentComparatorTest {

    OfferComment eldestComment;
    OfferComment medComment;
    OfferComment newestComment;

    @Before
    public void setUp() {
        this.eldestComment = CommentBuilderManager.aBasicCommentWithId(1L).build();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2015, 01, 05);
        this.eldestComment.setCommentCreationDate(calendar1.getTime());

        this.medComment = CommentBuilderManager.aBasicCommentWithId(2L).build();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2015, 01, 06);
        this.medComment.setCommentCreationDate(calendar2.getTime());

        this.newestComment = CommentBuilderManager.aBasicCommentWithId(3L).build();
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2015, 01, 07);
        this.newestComment.setCommentCreationDate(calendar3.getTime());
    }

    @Test
    public void testCommentsAreSortedByDateFromNewToOld() {
        List<OfferComment> comments = Arrays.asList(this.medComment, this.eldestComment, this.newestComment);
        Collections.sort(comments, new CommentComparator());
        assertThat(comments, contains(this.newestComment, this.medComment, this.eldestComment));
    }


}
