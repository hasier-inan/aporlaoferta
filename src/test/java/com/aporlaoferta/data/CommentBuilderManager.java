package com.aporlaoferta.data;

import com.aporlaoferta.model.OfferComment;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 19/01/2015
 * Time: 17:58
 */
public class CommentBuilderManager {

    public static CommentBuilder aBasicCommentWithId(Long id) {
        return CommentBuilder.aComment()
                .withId(id)
                .withCreationDate(new Date())
                .withOwner(UserBuilderManager.aRegularUserWithNickname("yourmother").build())
                .withCommentedOffer(OfferBuilderManager.aBasicOfferWithId(1L).build())
                .withText("dafuq is this")
                ;
    }

    public static CommentBuilder aBasicCommentWithIdAndOffer(Long offerId, Long id) {
        return CommentBuilder.aComment()
                .withId(id)
                .withCreationDate(new Date())
                .withOwner(UserBuilderManager.aRegularUserWithNickname("yourmother").build())
                .withCommentedOffer(OfferBuilderManager.aBasicOfferWithId(offerId).build())
                .withText("dafuq is this")
                ;
    }

    public static CommentBuilder aBasicCommentWithoutId() {
        return CommentBuilder.aComment()
                .withCreationDate(new Date())
                .withText("dafuq is this")
                ;
    }

    public static CommentBuilder aCommentWithQuotedCommentAndId(OfferComment offerComment, Long id) {
        return aBasicCommentWithId(id).withQuotedComment(offerComment);
    }
}
