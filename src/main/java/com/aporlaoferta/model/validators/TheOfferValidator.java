package com.aporlaoferta.model.validators;

import com.aporlaoferta.model.TheOffer;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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
    }
}
