package com.aporlaoferta.controller;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.offer.CommentManager;
import com.aporlaoferta.offer.UserManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import com.aporlaoferta.utils.RequestParameterParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class CommentController {

    private final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    @Qualifier("requestParameterParser")
    RequestParameterParser requestParameterParser;

    @Autowired
    OfferValidatorHelper offerValidatorHelper;

    @Autowired
    CommentManager commentManager;

    @Autowired
    UserManager userManager;

    @RequestMapping(value = "/createComment", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse createComment(@RequestBody OfferComment thatComment,
                                     @RequestParam(value = "offer", required = false) String offerId
    ) {
        TheResponse result = new TheResponse();
        String nickName = this.userManager.getUserNickNameFromSession();
        try {
            this.requestParameterParser.parseOfferCommentRawMap(thatComment, offerId, nickName);
            this.offerValidatorHelper.validateComment(thatComment);
            OfferComment comment = this.commentManager.createComment(thatComment);
            if (comment == null) {
                ControllerHelper.addEmptyDatabaseObjectMessage(result, LOG);
            } else {
                String okMessage = String.format("Comment successfully created. Id: %s", comment.getId());
                LOG.info(okMessage);
                result.assignResultCode(ResultCode.ALL_OK, okMessage);

            }
        } catch (ValidationException e) {
            String resultDescription = ResultCode.COMMENT_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.COMMENT_VALIDATION_ERROR);
        }
        return result;
    }

    @RequestMapping(value = "/quoteComment", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse quoteComment(@RequestBody OfferComment thatQuotedComment,
                                    @RequestParam(value = "quotedComment", required = true) String quotedCommentId
    ) {
        TheResponse result = new TheResponse();
        String nickName = this.userManager.getUserNickNameFromSession();
        try {
            this.requestParameterParser.parseOfferQuoteRawMap(thatQuotedComment, quotedCommentId, nickName);
            this.offerValidatorHelper.validateQuote(thatQuotedComment);
            OfferComment comment = this.commentManager.createComment(thatQuotedComment);
            if (comment == null) {
                ControllerHelper.addEmptyDatabaseObjectMessage(result, LOG);
            } else {
                String okMessage = String.format("Comment successfully created. Id: %s", comment.getId());
                LOG.info(okMessage);
                result.assignResultCode(ResultCode.ALL_OK, okMessage);
            }
        } catch (ValidationException e) {
            String resultDescription = ResultCode.QUOTE_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.QUOTE_VALIDATION_ERROR);
        }
        return result;
    }

}
