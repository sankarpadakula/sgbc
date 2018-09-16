package com.sp.sgbc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sp.sgbc.model.Applicant;

public interface ApplicantService {

  public Applicant findApplicantByEmail(String email);
  
  public Applicant findApplicantByBsnNum(String bsnNum);
  
  public Applicant findApplicantByConfirmationToken(String token);

  public Applicant getApplicantByName(String name);
  
  public Applicant save(Applicant applicant);
  
  public Applicant findOne(Long id);

  public List<Applicant> findAll();

  public Page<Applicant> findAll(Pageable pageable);
  
  public boolean exists(Long id);
  
  public List<Applicant> getUnpaidApplicants(int months);

  public void save(List<Applicant> applicants);
}
