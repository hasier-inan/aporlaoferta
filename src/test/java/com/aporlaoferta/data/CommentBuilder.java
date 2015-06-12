package com.aporlaoferta.data;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/11/2014
 * Time: 16:00
 */
public class CommentBuilder {

    private Long id;
    private Date commentCreationDate;
    private TheUser commentOwner;
    private TheOffer commentsOffer;
    private Long commentsQuotedComment;
    private String commentText;

    public static CommentBuilder aComment() {
        return new CommentBuilder();
    }

    public CommentBuilder() {
        super();
    }

    public CommentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CommentBuilder withCreationDate(Date creationDate) {
        this.commentCreationDate = creationDate;
        return this;
    }

    public CommentBuilder withOwner(TheUser owner) {
        this.commentOwner = owner;
        return this;
    }

    public CommentBuilder withCommentedOffer(TheOffer commentsOffer) {
        this.commentsOffer = commentsOffer;
        return this;
    }

    public CommentBuilder withQuotedComment(Long quotedComment) {
        this.commentsQuotedComment = quotedComment;
        return this;
    }

    public CommentBuilder withText(String text) {
        this.commentText = text;
        return this;
    }

    public OfferComment build() {
        OfferComment offerComment = new OfferComment();
        offerComment.setId(this.id);
        offerComment.setCommentsQuotedComment(this.commentsQuotedComment);
        offerComment.setCommentText(this.commentText);
        offerComment.setCommentsOffer(this.commentsOffer);
        offerComment.setCommentCreationDate(this.commentCreationDate);
        offerComment.setCommentOwner(this.commentOwner);
        return offerComment;
    }
}
