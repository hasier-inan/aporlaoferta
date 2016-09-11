package com.aporlaoferta.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
@Transactional(propagation = Propagation.REQUIRED)
public class CommentController {

    private final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private OfferValidatorHelper offerValidatorHelper;

    @Autowired
    private CommentManager commentManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private OfferManager offerManager;

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

    private TheResponse updateDeleteComment(OfferComment originalComment) {
        TheResponse result = new TheResponse();
        originalComment.setCommentText(ResultCode.COMMENT_DELETED_INFO.getResultDescriptionEsp());
        OfferComment updatedOfferComment = this.commentManager.saveComment(originalComment);
        ResponseResultHelper.updateResultWithSuccessCode(result, updatedOfferComment);
        return result;
    }

    private TheResponse updateCommentFromOriginal(OfferComment thatComment, OfferComment originalComment) {
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

    private void includeQuoteInComment(OfferComment thatComment, String commentId) {
        try {
            thatComment.setCommentsQuotedComment(Long.parseLong(commentId));
        } catch (NumberFormatException e) {
            LOG.error("Included quoted comment id is invalid", e);
        }
    }


    private void includeCommentInUser(OfferComment thatComment) throws InvalidUserException {
        String nickName = this.userManager.getUserNickNameFromSession();
        TheUser theUser = this.userManager.getUserFromNickname(nickName);
        if (!theUser.getEnabled()) {
            throw new InvalidUserException(theUser.getUserNickname());
        }
        theUser.addComment(thatComment);
    }

    private void includeCommentInQuotedOffer(OfferComment thatQuotedComment, String quotedCommentId) {
        try {
            OfferComment originalComment = this.commentManager.getCommentFromId(Long.parseLong(quotedCommentId));
            if (originalComment != null && originalComment.getCommentsOffer() != null) {
                thatQuotedComment.setCommentsOffer(originalComment.getCommentsOffer());
            }
        } catch (NumberFormatException e) {
            LOG.error("Included quoted comment id is invalid", e);
        }
    }

    private void includeCommentInOffer(OfferComment thatComment, String offerId) {
        try {
            TheOffer theOffer = this.offerManager.getOfferFromId(Long.parseLong(offerId));
            if (theOffer != null) {
                thatComment.setCommentsOffer(theOffer);
            }
        } catch (NumberFormatException e) {
            LOG.error("Included quoted comment id is invalid", e);
        }
    }

    private TheResponse validateAndPersistComment(OfferComment thatComment, ResultCode resultCodeError) {
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

    private void saveCommentAndUpdateResult(OfferComment thatComment, TheResponse result) {
        TheUser theUser = this.userManager.saveUser(thatComment.getCommentOwner());
        OfferComment comment = theUser.obtainLatestComment();
        if (comment == null) {
            ControllerHelper.addEmptyDatabaseObjectMessage(result, LOG);
        } else {
            ResponseResultHelper.updateResultWithSuccessCode(result, comment);
        }
    }


}
