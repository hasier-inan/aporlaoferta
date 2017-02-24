package com.aporlaoferta.controller;

import com.aporlaoferta.controller.helpers.DatabaseHelper;
import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CommentManager;
import com.aporlaoferta.service.InvalidUserException;
import com.aporlaoferta.service.OfferManager;
import com.aporlaoferta.service.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 2/02/14.
 */
public class CommentHelper {

    protected final Logger LOG = LoggerFactory.getLogger(CommentHelper.class);

    @Autowired
    protected OfferValidatorHelper offerValidatorHelper;

    @Autowired
    protected CommentManager commentManager;

    @Autowired
    protected UserManager userManager;

    @Autowired
    protected OfferManager offerManager;

    protected TheResponse updateDeleteComment(OfferComment originalComment) {
        TheResponse result = new TheResponse();
        originalComment.setCommentText(ResultCode.COMMENT_DELETED_INFO.getResultDescriptionEsp());
        OfferComment updatedOfferComment = this.commentManager.saveComment(originalComment);
        ResponseResultHelper.updateResultWithSuccessCode(result, updatedOfferComment);
        return result;
    }

    protected TheResponse updateCommentFromOriginal(OfferComment thatComment, OfferComment originalComment) {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheResponse result = new TheResponse();
        if (!originalComment.getCommentOwner().getUserNickname().equals(nickName)) {
            return ResponseResultHelper
                    .responseResultWithResultCodeError(ResultCode.INVALID_OWNER_ERROR, result);
        }
        String updatedText = thatComment.getCommentText();
        if (isEmpty(updatedText)) {
            return ResponseResultHelper
                    .responseResultWithResultCodeError(ResultCode.COMMENT_VALIDATION_ERROR, result);
        }
        originalComment.setCommentText(updatedText);
        OfferComment updatedOfferComment = this.commentManager.saveComment(originalComment);
        ResponseResultHelper.updateResultWithSuccessCode(result, updatedOfferComment);
        return result;
    }

    protected void includeQuoteInComment(OfferComment thatComment, String commentId) {
        try {
            thatComment.setCommentsQuotedComment(Long.parseLong(commentId));
        } catch (NumberFormatException e) {
            LOG.error("Included quoted comment id is invalid", e);
        }
    }


    protected void includeCommentInUser(OfferComment thatComment) throws InvalidUserException {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheUser theUser = this.userManager.getUserFromNickname(nickName);
        if (!theUser.getEnabled()) {
            throw new InvalidUserException(theUser.getUserNickname());
        }
        theUser.addComment(thatComment);
    }

    protected void includeCommentInQuotedOffer(OfferComment thatQuotedComment, String quotedCommentId) {
        try {
            OfferComment originalComment = this.commentManager.getCommentFromId(Long.parseLong(quotedCommentId));
            if (originalComment != null && originalComment.getCommentsOffer() != null) {
                thatQuotedComment.setCommentsOffer(originalComment.getCommentsOffer());
            }
        } catch (NumberFormatException e) {
            LOG.error("Included quoted comment id is invalid", e);
        }
    }

    protected void includeCommentInOffer(OfferComment thatComment, String offerId) {
        try {
            TheOffer theOffer = this.offerManager.getOfferFromId(Long.parseLong(offerId));
            if (theOffer != null) {
                thatComment.setCommentsOffer(theOffer);
            }
        } catch (NumberFormatException e) {
            LOG.error("Included quoted comment id is invalid", e);
        }
    }

    protected TheResponse validateAndPersistComment(OfferComment thatComment, ResultCode resultCodeError) {
        TheResponse result = new TheResponse();
        try {
            if (ResultCode.QUOTE_VALIDATION_ERROR.equals(
                    resultCodeError)) {
                this.offerValidatorHelper.validateQuote(thatComment);
            } else {
                this.offerValidatorHelper.validateComment(thatComment);
            }
            saveCommentAndUpdateResult(thatComment, result);
        } catch (ValidationException e) {
            ResponseResultHelper
                    .createErrorCodeResponseResult(resultCodeError, result, e);
        }
        return result;
    }

    protected void saveCommentAndUpdateResult(OfferComment thatComment, TheResponse result) {
        TheUser theUser = this.userManager.saveUser(thatComment.getCommentOwner());
        OfferComment comment = theUser.obtainLatestComment();
        if (comment == null) {
            DatabaseHelper.addEmptyDatabaseObjectMessage(result, LOG);
        } else {
            ResponseResultHelper.updateResultWithSuccessCode(result, comment);
        }
    }


}
