package com.aporlaoferta.model.validators;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 28/01/2015
 * Time: 18:02
 *
 */
public class ValidatorHelper {

    public static BindingResult getValidationErrors(Object o, Validator thatValidator) {
        DataBinder binder = new DataBinder(o);
        binder.setValidator(thatValidator);
        binder.validate();
        return binder.getBindingResult();
    }

}
