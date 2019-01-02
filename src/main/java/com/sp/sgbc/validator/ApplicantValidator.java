package com.sp.sgbc.validator;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.service.ApplicantService;
import com.sp.sgbc.util.DependentType;

@Component
public class ApplicantValidator implements Validator {
  @Autowired
  private ApplicantService applicantService;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private DependentValidator dependentValidator;

  @Override
  public boolean supports(Class<?> aClass) {
    return Applicant.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    Applicant app = (Applicant) o;

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Applicant Name Not Empty");
    Locale locale = LocaleContextHolder.getLocale();
    if (applicantService.findApplicantByBsnNum(app.getBsnNum()) != null) {
      String message = messageSource.getMessage("registration.user.exist", null, "User exist with same BSN number", locale);
      errors.rejectValue("name", message);
      return;
    }
    if ((app.getDocs() == null || app.getDocs().getOriginalFilename().isEmpty()) && (app.getFileName()== null || app.getFileName().isEmpty())) {
      String message = messageSource.getMessage("registration.docs.missing", null, "Applicant Proof Document is missed", locale);
      errors.rejectValue("docs", message);
    }
    if (app.getPartner() != null && "Married".equals(app.getMaritalStatus())) {
      try {
        app.getPartner().setType(DependentType.Spouse);
        errors.pushNestedPath("partner");
        ValidationUtils.invokeValidator(dependentValidator, app.getPartner(), errors);
      } finally {
        errors.popNestedPath();
      }
    }

    if (app.getChildrens() != null && !app.getChildrens().isEmpty()) {
      try {
        for (Dependent kid : app.getChildrens()) {
          kid.setType(DependentType.Child);
          errors.pushNestedPath("childrens[0]");
          dependentValidator.validate(kid, errors);
        }
      } finally {
        errors.popNestedPath();
      }
    }

 /*   if (app.getOtherContact() != null) {
      try {
        app.getOtherContact().setType(DependentType.OtherContact);
        errors.pushNestedPath("otherContact");
        dependentValidator.validate(app.getOtherContact(), errors);
      } finally {
        errors.popNestedPath();
      }
    }*/
  }
}
