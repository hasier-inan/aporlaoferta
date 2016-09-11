package com.aporlaoferta.controller;

import com.aporlaoferta.data.CommentBuilderManager;
import com.aporlaoferta.data.OfferBuilderManager;
import com.aporlaoferta.data.UserBuilderManager;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
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
    private static final String LOGGED_USER = "imtheloggedUser";

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
        when(this.userManager.getUserNickNameFromSession()).thenReturn(LOGGED_USER);
        TheUser theUser = UserBuilderManager.aRegularUserWithNickname(LOGGED_USER).build();
        theUser.addComment(CommentBuilderManager.aBasicCommentWithoutId().build());
        when(this.userManager.getUserFromNickname(LOGGED_USER)).thenReturn(theUser);
        when(this.userManager.saveUser(any(TheUser.class))).thenReturn(theUser);
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
        verify(this.userManager).saveUser(offerComment.getCommentOwner());
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
        when(this.userManager.saveUser(any(TheUser.class))).thenReturn(UserBuilderManager.aRegularUserWithNickname("as").build());
        TheResponse result = this.commentController.createComment(CommentBuilderManager.aBasicCommentWithId(55L).build(), OFFER_ID);
        assertTrue(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getCode() == result.getCode());
        assertEquals(result.getDescription(), ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getResultDescription());
    }

    @Test
    public void testSuccessfullyCreatedCommentContainsOKResult() {
        long id = 2L;
        OfferComment offerComment = CommentBuilderManager.aBasicCommentWithId(id).build();
        TheUser theUser = UserBuilderManager.aRegularUserWithNickname("sdaaf").build();
        theUser.addComment(offerComment);
        when(this.userManager.saveUser(any(TheUser.class))).thenReturn(theUser);
        TheResponse result = this.commentController.createComment(offerComment, OFFER_ID);
        assertTrue(ResultCode.ALL_OK.getCode() == result.getCode());
        assertThat(result.getDescription(), Matchers.is("Comment successfully created. Id: " + id));
    }

    @Test
    public void testCreateCommentIsForNonBannedUsersOnly() {
        userIsBanned();
        TheResponse result = this.commentController.createComment(CommentBuilderManager.aBasicCommentWithoutId().build(), "1");
        assertTrue(ResultCode.USER_BANNED.getCode() == result.getCode());
    }

    @Test
    public void testQuoteCommentIsForNonBannedUsersOnly() {
        userIsBanned();
        TheResponse result = this.commentController.quoteComment(CommentBuilderManager.aBasicCommentWithoutId().build(), "1");
        assertTrue(ResultCode.USER_BANNED.getCode() == result.getCode());
    }

    @Test
    public void testUpdateCommentIsForNonBannedUsersOnly() {
        when(this.userManager.userIsBanned()).thenReturn(true);
        TheResponse result = this.commentController.updateComment(CommentBuilderManager.aBasicCommentWithoutId().build(), "1");
        assertTrue(ResultCode.USER_BANNED.getCode() == result.getCode());
    }

    @Test
    public void testDeleteCommentIsForAdminOnly() {
        OfferComment theComment = CommentBuilderManager.aBasicCommentWithId(3L).build();
        when(this.commentManager.getCommentFromId(3L)).thenReturn(theComment);
        when(this.commentManager.saveComment(theComment)).thenReturn(theComment);
        TheResponse result = this.commentController.deleteComment("3");
        assertTrue(ResultCode.DEFAULT_ERROR.getCode() == result.getCode());
    }

    @Test
    public void testDeleteCommentWhenCommentDoesNotExistReturnsValidationError() {
        when(this.userManager.isUserAdmin()).thenReturn(true);
        when(this.commentManager.getCommentFromId(3L)).thenReturn(null);
        TheResponse result = this.commentController.deleteComment("3");
        assertTrue(ResultCode.UPDATE_COMMENT_VALIDATION_ERROR.getCode() == result.getCode());
    }

    @Test
    public void testDeleteCommentWhenCommentIdIsWrongReturnsValidationError() {
        when(this.userManager.isUserAdmin()).thenReturn(true);
        TheResponse result = this.commentController.deleteComment("wrong");
        assertTrue(ResultCode.UPDATE_COMMENT_VALIDATION_ERROR.getCode() == result.getCode());
    }

    @Test
    public void testDeleteCommentIsReturnsOKResult() {
        when(this.userManager.isUserAdmin()).thenReturn(true);
        OfferComment theComment = CommentBuilderManager.aBasicCommentWithId(3L).build();
        when(this.commentManager.getCommentFromId(3L)).thenReturn(theComment);
        when(this.commentManager.saveComment(theComment)).thenReturn(theComment);
        TheResponse result = this.commentController.deleteComment("3");
        assertTrue(ResultCode.ALL_OK.getCode() == result.getCode());
    }

    private void userIsBanned() {
        TheUser bannedUser = UserBuilderManager.aRegularUserWithNickname(LOGGED_USER).isEnabled(false).build();
        when(this.userManager.getUserFromNickname(LOGGED_USER)).thenReturn(bannedUser);
    }

}
