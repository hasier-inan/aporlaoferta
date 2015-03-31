package com.aporlaoferta.controller;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.offer.CommentManager;
import com.aporlaoferta.offer.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import com.aporlaoferta.utils.RequestParameterParser;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by hasiermetal on 29/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentControllerTest {

    private static final String OFFER_ID = "2";
    @InjectMocks
    CommentController commentController;
    @Mock
    OfferValidatorHelper offerValidatorHelper;
    @Mock
    RequestParameterParser requestParameterParser;
    @Mock
    UserManager userManager;
    @Mock
    CommentManager commentManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(this.userManager.getUserNickNameFromSession()).thenReturn("imtheloggedUser");
    }

    @Test
    public void testValidationIsExecuted() {
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithoutId().build();
        this.commentController.createComment(offerComment, OFFER_ID);
        verify(this.offerValidatorHelper).validateComment(offerComment);
    }

    @Test
    public void testManagerHandlesComment() {
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithoutId().build();
        this.commentController.createComment(offerComment, OFFER_ID);
        verify(this.commentManager).createComment(offerComment);
    }

    @Test
    public void testCommentIsNotCreatedIfValidationExceptionOccurs() {
        doThrow(new ValidationException("error")).when(this.offerValidatorHelper)
                .validateComment((OfferComment) anyObject());
        TheResponse result = this.commentController.createComment(new OfferComment(), OFFER_ID);
        assertTrue(result.getResponseResult() == ResultCode.COMMENT_VALIDATION_ERROR.getResponseResult());
        assertEquals(result.getDescription(), "Validation process failed while creating comment");
    }

    @Test
    public void testNotSavedCommentContainsExpectedResultCode() {
        when(this.commentManager.createComment(any(OfferComment.class))).thenReturn(null);
        TheResponse result = this.commentController.createComment(new OfferComment(), OFFER_ID);
        assertTrue(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getCode() == result.getCode());
        assertEquals(result.getDescription(), ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getResultDescription());
    }

    @Test
    public void testSuccessfullyCreatedCommentContainsOKResult() {
        long id = 2L;
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithId(id).build();
        when(this.commentManager.createComment(any(OfferComment.class))).thenReturn(offerComment);
        TheResponse result = this.commentController.createComment(offerComment, OFFER_ID);
        assertTrue(ResultCode.ALL_OK.getCode() == result.getCode());
        assertThat(result.getDescription(), Matchers.is("Comment successfully created. Id: " + id));
    }

}
