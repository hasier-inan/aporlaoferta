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
        result.assignResultCode(ResultCode.ALL_OK, okMessage, "Comentario añadido");
    }

    public static TheResponse createInvalidCaptchaResponse() {
        return updateWithCode(ResultCode.INVALID_CAPTCHA);
    }

    public static TheResponse createExistingEmailResponse() {
        return updateWithCode(ResultCode.USER_EMAIL_ALREADY_EXISTS);
    }

    public static TheResponse createUnhealthyResponse() {
        return updateWithCode(ResultCode.UNHEALTHY_ENDPOINT);
    }

    public static TheResponse createDefaultResponse() {
        return updateWithCode(ResultCode.DEFAULT_ERROR);
    }

    public static TheResponse createInvalidUUIDResponse() {
        return updateWithCode(ResultCode.INVALID_CONFIRMATION_ID);
    }

    public static TheResponse createInvalidUserResponse() {
        return updateWithCode(ResultCode.USER_BANNED);
    }

    public static TheResponse offerUpdateResponse() {
        TheResponse theResponse = new TheResponse();
        theResponse.assignResultCode(ResultCode.ALL_OK, "Offer updated", "Oferta actualizada");
        return theResponse;
    }

    public static TheResponse offerValidationErrorResponse() {
        TheResponse theResponse = new TheResponse();
        theResponse.assignResultCode(ResultCode.CREATE_OFFER_VALIDATION_ERROR);
        return theResponse;
    }

    public static TheResponse createForgottenPasswordResponse() {
        TheResponse theResponse = new TheResponse();
        theResponse.assignResultCode(ResultCode.ALL_OK, "Password reset email request has been sent", "En breve recibirás un correo electrónico donde podrás reiniciar tu contraseña");
        return theResponse;
    }

    public static TheResponse createUserConfirmationResponse() {
        TheResponse theResponse = new TheResponse();
        theResponse.assignResultCode(ResultCode.ALL_OK, "User has been confirmed", "Usuario confirmado, ya puedes identificarte");
        return theResponse;
    }

    public static TheResponse createUserBannedConfirmationResponse() {
        TheResponse theResponse = new TheResponse();
        theResponse.assignResultCode(ResultCode.ALL_OK, "User has been banned", "Usuario expulsado");
        return theResponse;
    }

    public static TheResponse updateWithCode(ResultCode resultCode) {
        TheResponse theResponse = new TheResponse();
        theResponse.setCode(resultCode.getCode());
        theResponse.setDescriptionEsp(resultCode.getResultDescriptionEsp());
        theResponse.setResponseResult(resultCode.getResponseResult());
        return theResponse;
    }
}
