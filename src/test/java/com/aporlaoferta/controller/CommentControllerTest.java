package com.aporlaoferta.controller;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CommentManager;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    UserManager userManager;
    @Mock
    OfferManager offerManager;
    @Mock
    CommentManager commentManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        String imtheloggedUser = "imtheloggedUser";
        when(this.userManager.getUserNickNameFromSession()).thenReturn(imtheloggedUser);
        when(this.userManager.getUserFromNickname(imtheloggedUser)).thenReturn(UserBuilderManager.aRegularUserWithNickname(imtheloggedUser).build());
        when(this.offerManager.getOfferFromId(anyLong())).thenReturn(OfferBuilderManager.aBasicOfferWithoutId().build());
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
