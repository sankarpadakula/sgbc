package com.sp.sgbc.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.service.ApplicantService;

@Component
public class ApplicantValidator implements Validator {
    @Autowired
    private ApplicantService applicantService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Applicant.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
      Applicant app = (Applicant) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
       
        if (applicantService.findApplicantByEmail(app.getEmail()) != null) {
            errors.rejectValue("name", "Duplicate.userForm.username");
        }

       
    }
}
