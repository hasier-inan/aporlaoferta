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
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "companyUrl", ValidationError.ERROR_FIELD_REQUIRED.getCode());
    }
}
