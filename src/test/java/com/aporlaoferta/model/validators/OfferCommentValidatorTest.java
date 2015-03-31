package com.aporlaoferta.model.validators;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OfferCommentValidatorTest {

    OfferCommentValidator offerCommentValidator;
    OfferComment offerComment;

    @Before
    public void setUp() {
        this.offerCommentValidator = new OfferCommentValidator();
        this.offerComment = CommentBuilderManager.aBasicCommentWithId(3L).build();
    }

    @Test
    public void testOwnerIsMandatory() {
        this.offerComment.setCommentOwner(null);
        assertTrue(ValidatorHelper.getValidationErrors(this.offerComment, this.offerCommentValidator).hasErrors());
    }

    @Test
    public void testCommentTextIsMandatory() {
        this.offerComment.setCommentText(null);
        assertTrue(ValidatorHelper.getValidationErrors(this.offerComment, this.offerCommentValidator).hasErrors());
    }

    @Test
    public void testCommentOfferIsMandatory() {
        this.offerComment.setCommentsOffer(null);
        assertTrue(ValidatorHelper.getValidationErrors(this.offerComment, this.offerCommentValidator).hasErrors());
    }

    @Test
    public void testCommentDateIsMandatory() {
        this.offerComment.setCommentCreationDate(null);
        assertTrue(ValidatorHelper.getValidationErrors(this.offerComment, this.offerCommentValidator).hasErrors());
    }

    @Test
    public void testMinimumParametersIncludedThrowsNoError() {
        assertFalse(ValidatorHelper.getValidationErrors(this.offerComment, this.offerCommentValidator).hasErrors());
    }

}