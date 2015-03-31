package com.aporlaoferta.model.validators;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OfferCommentQuoteValidatorTest {

    OfferCommentQuoteValidator offerCommentQuoteValidator;
    OfferComment offerComment;

    @Before
    public void setUp() {
        this.offerCommentQuoteValidator = new OfferCommentQuoteValidator();
    }

    @Test
    public void testQuoteIsRejectedInAnEmptyComment() {
        this.offerComment = new OfferComment();
        BindingResult bindingResult = ValidatorHelper.getValidationErrors(this.offerComment, this.offerCommentQuoteValidator);
        assertTrue(bindingResult.hasErrors());
    }

    @Test
    public void testQuoteIsRejectedIfNoQuotedCommentIsIncluded() {
        this.offerComment = CommentBuilderManager.aBasicCommentWithoutId().build();
        BindingResult bindingResult = ValidatorHelper.getValidationErrors(this.offerComment,
                this.offerCommentQuoteValidator);
        assertTrue(bindingResult.hasErrors());
    }

    @Test
    public void testQuoteIsAcceptedIfQuotedCommentIsIncluded() {
        this.offerComment = CommentBuilderManager.aCommentWithQuotedCommentAndId(
                CommentBuilderManager.aBasicCommentWithoutId().build(), 3L).build();
        BindingResult bindingResult = ValidatorHelper.getValidationErrors(this.offerComment,
                this.offerCommentQuoteValidator);
        assertFalse(bindingResult.hasErrors());
    }

}