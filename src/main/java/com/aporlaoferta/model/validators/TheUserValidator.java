package com.aporlaoferta.model.validators;

import com.aporlaoferta.model.TheUser;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class TheUserValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TheUser.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userNickname",
                ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userPassword",
                ValidationError.ERROR_FIELD_REQUIRED.getCode());
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userEmail", ValidationError.ERROR_FIELD_REQUIRED.getCode());
    }
}
