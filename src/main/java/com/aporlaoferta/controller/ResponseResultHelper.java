package com.aporlaoferta.controller;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.validators.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 01/05/15
 * Time: 15:09
 */
public class ResponseResultHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseResultHelper.class);

    public static TheResponse responseResultWithResultCodeError(ResultCode resultCodeError, TheResponse theResponse) {
        createErrorCodeResponseResult(resultCodeError, theResponse, new ValidationException(resultCodeError.name()));
        return theResponse;
    }

    public static void createErrorCodeResponseResult(ResultCode resultCodeError, TheResponse result, ValidationException e) {
        String resultDescription = resultCodeError.getResultDescription();
        LOG.warn(resultDescription, e);
        result.assignResultCode(resultCodeError);
    }

    public static void updateResultWithSuccessCode(TheResponse result, OfferComment comment) {
        String okMessage = String.format("Comment successfully created. Id: %s", comment.getId());
        LOG.info(okMessage);
        result.assignResultCode(ResultCode.ALL_OK, okMessage, "Comentario añadido");
    }

    public static TheResponse createInvalidCaptchaResponse() {
        return updateWithCode(ResultCode.INVALID_CAPTCHA);
    }

    public static TheResponse createUnhealthyResponse() {
        return updateWithCode(ResultCode.UNHEALTHY_ENDPOINT);
    }

    public static TheResponse createDefaultResponse() {
        return updateWithCode(ResultCode.DEFAULT_ERROR);
    }

    private static TheResponse updateWithCode(ResultCode resultCode) {
        TheResponse theResponse = new TheResponse();
        theResponse.setCode(resultCode.getCode());
        theResponse.setDescriptionEsp(resultCode.getResultDescriptionEsp());
        theResponse.setResponseResult(resultCode.getResponseResult());
        return theResponse;
    }
}
