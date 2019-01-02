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
    if (dep.getType() == DependentType.Spouse) {
      String name = messageSource.getMessage("registration.spousename.notempty", null, "Spouse Full name should not empty", locale);
      String gender = messageSource.getMessage("registration.spousegender.notempty", null, "Spouse Gender should not empty", locale);
      String bsnNum = messageSource.getMessage("registration.spousebsn.notempty", null, "Spouse BSN Number should not empty", locale);
      String dob = messageSource.getMessage("registration.spousedob.notempty", null, "Spouse Date of birth should not empty", locale);
      String pob = messageSource.getMessage("registration.spousepob.notempty", null, "Spouse Plance of birth should not empty", locale);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", name);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", gender);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "placeOfBirth", pob);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", dob);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bsnNum", bsnNum);
      if ((dep.getDocs() == null || dep.getDocs().getOriginalFilename().isEmpty()) && (dep.getFileName() == null || dep.getFileName().isEmpty())) {
        String message = messageSource.getMessage("registration.spousedocs.notempty", null, "Spouse Proof Document is missed", locale);
        errors.rejectValue("docs", message);
      }
    } else if (dep.getType() == DependentType.Child) {
      String name = messageSource.getMessage("registration.kidname.notempty", null, "Kid Full name should not empty", locale);
      String gender = messageSource.getMessage("registration.kidgender.notempty", null, "Kid Gender should not empty", locale);
      String bsnNum = messageSource.getMessage("registration.kidbsn.notempty", null, "Kid BSN Number should not empty", locale);
      String dob = messageSource.getMessage("registration.kiddob.notempty", null, "Kid Date of birth should not empty", locale);
      String pob = messageSource.getMessage("registration.kidpob.notempty", null, "Kid Place of birth should not empty", locale);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", name);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", gender);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "placeOfBirth", pob);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateOfBirth", dob);
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bsnNum", bsnNum);
      if ((dep.getDocs() == null || dep.getDocs().getOriginalFilename().isEmpty()) && (dep.getFileName() == null || dep.getFileName().isEmpty())) {
        String message = messageSource.getMessage("registration.kiddocs.notempty", null, "Kid Proof Document is missed", locale);
        errors.rejectValue("docs", message);
      }
    }
    if (dep.getType() == DependentType.Child && dep.getDateOfBirth() != null && Helper.monthsDiffFromCurrent(dep.getDateOfBirth()) > 217) {
      String message = messageSource.getMessage("registration.kid.overage", new String[] { dep.getName() }, "Kid having morethan 18 years, Hence should not be a dependent", locale);
      errors.rejectValue("dateOfBirth", message);
    }
  }

}
