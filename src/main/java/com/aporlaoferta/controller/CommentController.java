package com.aporlaoferta.controller;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.service.InvalidUserException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
@Transactional(propagation = Propagation.REQUIRED)
public class CommentController extends CommentHelper {

    @RequestMapping(value = "/createComment", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse createComment(@RequestBody OfferComment thatComment,
                                     @RequestParam(value = "offer", required = true) String offerId
    ) {
        try {
            includeCommentInUser(thatComment);
            includeCommentInOffer(thatComment, offerId);
            thatComment.setCommentCreationDate(new Date());
            TheResponse theResponse = validateAndPersistComment(thatComment, ResultCode.COMMENT_VALIDATION_ERROR);
            return theResponse;
        } catch (InvalidUserException e) {
            LOG.error("Banned user request catch. User details: ", e.getMessage());
        }
        return ResponseResultHelper.createInvalidUserResponse();
    }

    @RequestMapping(value = "/quoteComment", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse quoteComment(@RequestBody OfferComment thatQuotedComment,
                                    @RequestParam(value = "quotedComment", required = true) String quotedCommentId
    ) {
        try {
            includeCommentInUser(thatQuotedComment);
            includeQuoteInComment(thatQuotedComment, quotedCommentId);
            includeCommentInQuotedOffer(thatQuotedComment, quotedCommentId);
            thatQuotedComment.setCommentCreationDate(new Date());
            return validateAndPersistComment(thatQuotedComment, ResultCode.QUOTE_VALIDATION_ERROR);
        } catch (InvalidUserException e) {
            LOG.error("Banned user request catch. User details: ", e.getMessage());
        }
        return ResponseResultHelper.createInvalidUserResponse();
    }

    @RequestMapping(value = "/updateComment", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse updateComment(@RequestBody OfferComment thatComment,
                                     @RequestParam(value = "comment", required = false) String commentId
    ) {
        if (this.userManager.userIsBanned()) {
            return ResponseResultHelper.createInvalidUserResponse();
        }

        OfferComment originalComment = null;
        try {
            originalComment = this.commentManager.getCommentFromId(Long.parseLong(commentId));
        } catch (NumberFormatException e) {
            LOG.error("Could not parse invalid comment id: ", e);
        }
        if (originalComment == null) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.UPDATE_COMMENT_VALIDATION_ERROR, new TheResponse());
        }

        return updateCommentFromOriginal(thatComment, originalComment);
    }

    @RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse deleteComment(@RequestParam(value = "comment", required = true) String commentId
    ) {
        if (!this.userManager.isUserAdmin()) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.DEFAULT_ERROR, new TheResponse());
        }
        OfferComment originalComment = null;
        try {
            originalComment = this.commentManager.getCommentFromId(Long.parseLong(commentId));
        } catch (NumberFormatException e) {
            LOG.error("Could not parse invalid comment id: ", e);
        }
        if (originalComment == null) {
            return ResponseResultHelper.
                    responseResultWithResultCodeError(ResultCode.UPDATE_COMMENT_VALIDATION_ERROR, new TheResponse());
        }
        return updateDeleteComment(originalComment);
    }

}
