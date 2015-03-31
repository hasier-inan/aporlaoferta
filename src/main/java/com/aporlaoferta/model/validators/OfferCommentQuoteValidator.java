package com.aporlaoferta.model.validators;

import com.aporlaoferta.model.OfferComment;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by hasiermetal on 15/01/15.
 */
public class OfferCommentQuoteValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return OfferComment.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "commentsQuotedComment", ValidationError.ERROR_FIELD_REQUIRED.getCode());
    }
}
