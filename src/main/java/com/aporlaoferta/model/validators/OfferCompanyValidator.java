package com.aporlaoferta.model.validators;

import com.aporlaoferta.model.OfferCompany;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class OfferCompanyValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return OfferCompany.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyName",
                ValidationError.ERROR_FIELD_REQUIRED.getCode());
        OfferCompany offerCompany=(OfferCompany)o;
        if (offerCompany.getCompanyName().length() > 250) {
            errors.rejectValue("companyName", "error.fieldFormat", ValidationError.ERROR_FIELD_LENGTH_EXCEED.getCode());
        }
    }
}
