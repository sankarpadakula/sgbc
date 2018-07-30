package com.sp.sgbc.validator;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.util.DependentType;
import com.sp.sgbc.util.Helper;

@Component
public class DependentValidator implements Validator {

  @Autowired
  private MessageSource messageSource;

  @Override
  public boolean supports(Class<?> clazz) {
    return Dependent.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Dependent dep = (Dependent) target;
    Locale locale = LocaleContextHolder.getLocale();
    String notEmpty = messageSource.getMessage("not.empty", null, "Not empty", locale);
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", notEmpty);
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", notEmpty);
    if (dep.getType() != DependentType.OtherContact) {
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "placeOfBirth", notEmpty);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", notEmpty);
    } else {
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", notEmpty);
    }
    if (dep.getType() == DependentType.Child && dep.getDateOfBirth() != null
        && Helper.monthsDiffFromCurrent(dep.getDateOfBirth()) > 217) {
      String message = messageSource.getMessage("registration.kid.overage", new String[] { dep.getName() },
          "Kid having morethan 18 years, Hence should not be a dependent", locale);
      errors.rejectValue("dateOfBirth", message);
    }
  }

}
