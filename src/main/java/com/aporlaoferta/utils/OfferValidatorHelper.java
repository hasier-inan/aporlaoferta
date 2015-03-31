package com.aporlaoferta.utils;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;
import com.aporlaoferta.model.validators.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class OfferValidatorHelper {

    private final Logger LOG = LoggerFactory.getLogger(OfferValidatorHelper.class);

    private final OfferCompanyValidator offerCompanyValidator;
    private final TheUserValidator theUserValidator;
    private final TheOfferValidator theOfferValidator;
    private final OfferCommentValidator offerCommentValidator;
    private final OfferCommentQuoteValidator offerCommentQuoteValidator;

    public OfferValidatorHelper(OfferCompanyValidator offerCompanyValidator, TheUserValidator theUserValidator,
                                TheOfferValidator theOfferValidator, OfferCommentValidator offerCommentValidator,
                                OfferCommentQuoteValidator offerCommentQuoteValidator) {
        this.offerCompanyValidator = offerCompanyValidator;
        this.theUserValidator = theUserValidator;
        this.theOfferValidator = theOfferValidator;
        this.offerCommentValidator = offerCommentValidator;
        this.offerCommentQuoteValidator = offerCommentQuoteValidator;
    }

    /**
     * Validates company object checking mandatory fields
     *
     * @param offerCompany: company object to be validated
     * @throws ValidationException if any error is found (it will be logged)
     */
    public void validateCompany(OfferCompany offerCompany) throws ValidationException {
        BindingResult bindingResult = getValidationErrors(offerCompany, this.offerCompanyValidator);
        checkForValidationErrors(bindingResult, "The company");
    }

    /**
     * Validates an offer object created by a customer
     *
     * @param theOffer to be validated
     * @throws ValidationException if minimum fields are not defined in the domain object
     */
    public void validateOffer(TheOffer theOffer) throws ValidationException {
        BindingResult bindingResult = getValidationErrors(theOffer, this.theOfferValidator);
        checkForValidationErrors(bindingResult, "The offer");
    }

    /**
     * validates a comment created by a user
     *
     * @param offerComment: Comment offer
     * @throws ValidationException if mandatory fields are not included (although front-end validation will be
     * performed)
     */
    public void validateComment(OfferComment offerComment) throws ValidationException {
        BindingResult bindingResult = getValidationErrors(offerComment, this.offerCommentValidator);
        checkForValidationErrors(bindingResult, "The comment");
    }

    /**
     * Validates a quoted comment
     *
     * @param offerComment: comment with quoted comment inside
     * @throws ValidationException if required fields are empty
     */
    public void validateQuote(OfferComment offerComment) throws ValidationException {
        BindingResult bindingResult = getValidationErrors(offerComment, this.offerCommentQuoteValidator);
        checkForValidationErrors(bindingResult, "The quote");
    }

    /**
     * Validates the user checking if mandatory fields are included within the object
     *
     * @param theUser : target user
     * @throws ValidationException if any error is found
     */
    public void validateUser(TheUser theUser) throws ValidationException {
        BindingResult bindingResult = getValidationErrors(theUser, this.theUserValidator);
        checkForValidationErrors(bindingResult, "The user");
    }

    private void checkForValidationErrors(BindingResult bindingResult, String targetName) {
        if (bindingResult.hasErrors()) {
            ObjectError firstError = getFirstObjectError(bindingResult);
            String errorMessage = String.format("%s validation process failed: '%s'", targetName,
                    firstError.toString());
            LOG.warn(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    private ObjectError getFirstObjectError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().get(0);
    }

    private BindingResult getValidationErrors(Object o, Validator thatValidator) {
        DataBinder binder = new DataBinder(o);
        binder.setValidator(thatValidator);
        binder.validate();
        return binder.getBindingResult();
    }
}
