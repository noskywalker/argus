package com.monitor.argus.bean;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BeanValidator {
    private Validator vr = Validation.buildDefaultValidatorFactory().getValidator();

    public <T> void validate(T e) throws ValidationException {
        Set<ConstraintViolation<T>> set = vr.validate(e);
        if (set.isEmpty()) {
            return;
        }
        StringBuilder err = new StringBuilder();
        for (ConstraintViolation<T> cv : set) {
            err.append(",");
            err.append(cv.getPropertyPath());
            err.append(" ");
            err.append(cv.getMessage());
        }
        throw new ValidationException(err.substring(1).toString());
    }

    public <T> void validate(Collection<T> collection) throws ValidationException {
        for (T e : collection) {
            validate(e);
        }
    }

}
