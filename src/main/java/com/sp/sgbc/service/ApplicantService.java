package com.sp.sgbc.service;

import java.util.List;
import java.util.Optional;

import com.sp.sgbc.model.Applicant;

public interface ApplicantService {

  public Applicant findApplicantByEmail(String email);
  
  public Applicant findApplicantByConfirmationToken(String token);
  
  public Applicant saveApplicant(Applicant applicant);
  
  public Applicant getApplicantByName(String name);
  
  public Applicant getApplicantById(Long id);

  public List<Applicant> getAllApplicants();
}
