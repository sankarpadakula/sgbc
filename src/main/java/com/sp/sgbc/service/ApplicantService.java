package com.sp.sgbc.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sp.sgbc.model.Applicant;

public interface ApplicantService {

  public Applicant findApplicantByEmail(String email);
  
  public Applicant findApplicantByBsnNum(String bsnNum);
  
  public Applicant findByUid(String token);

  public void deleteByUid(String token);
  
  public Applicant getApplicantByName(String name);
  
  public Applicant save(Applicant applicant);
  
  public Applicant findOne(Long id);

  public List<Applicant> findAll();

  public Page<Applicant> findAll(Pageable pageable);
  
  public boolean exists(Long id);
  
  public List<Applicant> getUnpaidApplicants(int months);

  public void save(List<Applicant> applicants);

  public String save(String uid, @Valid Applicant user);
}
