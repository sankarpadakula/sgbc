package com.sp.sgbc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.repository.ApplicantRepository;

@Service("applicantService")
public class ApplicantServiceImpl implements ApplicantService{

  @Autowired
  private ApplicantRepository applicantRepository;
  
  public Applicant findApplicantByEmail(String email) {
    return applicantRepository.findByEmail(email);
  }

  public Applicant saveApplicant(Applicant applicant) {
    return applicantRepository.save(applicant);
  }

  public Applicant getApplicantById(Long id) {
    return applicantRepository.findById(id).orElse(null);
  }
  
  @Override
  public Applicant getApplicantByName(String name) {
    return applicantRepository.findByName(name);
  }

  @Override
  public List<Applicant> getAllApplicants() {
    return applicantRepository.findAll();
  }

  @Override
  public Applicant findApplicantByConfirmationToken(String token) {
    return applicantRepository.findByConfirmationToken(token);
  }

}
