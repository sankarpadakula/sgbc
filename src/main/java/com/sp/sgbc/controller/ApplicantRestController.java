package com.sp.sgbc.controller;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.service.ApplicantService;

@RestController
@RequestMapping("/rest")
public class ApplicantRestController {

  @Autowired
  private ApplicantService applicantService;
  
  @RequestMapping(path="/applicants", method = {RequestMethod.GET})
  public List<Applicant> getAllApplicants(){
    List<Applicant> list = applicantService.getAllApplicants();
    for (Applicant applicant : list) {
      applicant.setPartner(null);
      applicant.setChildrens(null);
    }
      return list;
  }
  
  @RequestMapping(path="/applicants", method = {RequestMethod.POST})
  public List<Applicant> allApplicants(){
    List<Applicant> list = applicantService.getAllApplicants();
    for (Applicant applicant : list) {
      applicant.setPartner(null);
      applicant.setChildrens(null);
    }
      return list;
  }
  
  @RequestMapping(value = "/applicants/{id}", method = {RequestMethod.GET})
  public Applicant getApplicantById(@PathVariable("id") Long id){
      return applicantService.getApplicantById(id);
  }
  
  @RequestMapping(value = "/applicants/{id}", method = {RequestMethod.POST})
  public Applicant postApplicantById(@PathVariable("id") Long id, Applicant applicant){
      return applicantService.saveApplicant(applicant);
  }
}
