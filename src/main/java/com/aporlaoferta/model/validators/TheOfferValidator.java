package com.aporlaoferta.model.validators;

import com.aporlaoferta.model.TheOffer;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class TheOfferValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TheOffer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "offerTitle", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "finalPrice", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "offerImage", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "offerDescription",
                ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "offerLink", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        //ValidationUtils.rejectIfEmpty(errors, "offerCreatedDate", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmpty(errors, "offerCompany", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmpty(errors, "offerCategory", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmpty(errors, "offerUser", ValidationError.ERROR_FIELD_REQUIRED.getCode());
        validateLength((TheOffer) o, errors);
    }

    private void validateLength(TheOffer theOffer, Errors errors) {
        if (!isEmpty(theOffer.getOfferTitle()) && theOffer.getOfferTitle().length() > 250) {
            errors.rejectValue("offerTitle", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
        }
        if (!isEmpty(theOffer.getOfferDescription()) && theOffer.getOfferDescription().length() > 2000) {
            errors.rejectValue("offerDescription", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
        }
        if (!isEmpty(theOffer.getOfferLink()) && theOffer.getOfferLink().length() > 2000) {
            errors.rejectValue("offerLink", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
        }
        if (!isEmpty(theOffer.getOriginalPrice())) {
            if (theOffer.getOriginalPrice().compareTo(new BigDecimal(999999)) > 0) {
                errors.rejectValue("originalPrice", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
            }
            if (theOffer.getOriginalPrice().compareTo(new BigDecimal(0)) < 0) {
                errors.rejectValue("originalPrice", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
            }
        }
        if (!isEmpty(theOffer.getFinalPrice()) && theOffer.getFinalPrice().compareTo(new BigDecimal(999999)) > 0) {
            errors.rejectValue("finalPrice", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
        }
        if (!isEmpty(theOffer.getFinalPrice()) && theOffer.getFinalPrice().compareTo(new BigDecimal(0)) < 0) {
            errors.rejectValue("finalPrice", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
        }
    }
}
