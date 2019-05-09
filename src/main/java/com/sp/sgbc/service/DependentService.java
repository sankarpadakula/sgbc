package com.sp.sgbc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.repository.ApplicantRepository;
import com.sp.sgbc.repository.DependentRepository;

@Service("dependentService")
public class DependentService {

  @Autowired
  private DependentRepository dependentRepository;

  @Autowired
  private ApplicantRepository applicantRepository;

  public Dependent save(Dependent dependent) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      dependent.setModifiedBy(auth.getName());
      dependent.setModifiedDate(new Date());
    }
    return dependentRepository.save(dependent);
  }

  public Dependent findOne(Long id) {
    return dependentRepository.findById(id).orElse(null);
  }
  
  @Transactional(value = TxType.REQUIRES_NEW)
  public List<Dependent> getDependentsByApplicantId(Long appId) {
    Applicant applicant = applicantRepository.findById(appId).orElse(new Applicant());
    Dependent partner = applicant.getPartner();
    Dependent contact = applicant.getOtherContact();
    List<Dependent> dependents = dependentRepository.findByApplicantId(appId);
    if (dependents == null) {
      dependents = new ArrayList<Dependent>();
    } else {
      for (Dependent dependent : dependents) {
        if (dependent.getModifiedDate() == null) {
          dependent.setModifiedDate(dependent.getCreatedDate());
          dependent.setModifiedBy(dependent.getCreatedBy());
        }
      }
    }
    if (partner != null) {
      if (partner.getModifiedDate() == null) {
        partner.setModifiedDate(partner.getCreatedDate());
        partner.setModifiedBy(partner.getCreatedBy());
      }
      dependents.add(partner);
    }
    if (contact != null) {
      if (contact.getModifiedDate() == null) {
        contact.setModifiedDate(contact.getCreatedDate());
        contact.setModifiedBy(contact.getCreatedBy());
      }
      dependents.add(contact);
    }
    return dependents;
  }

  public boolean exists(Long id) {
    return dependentRepository.existsById(id);
  }

  public void save(List<Dependent> dependents) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    for (Dependent dependent : dependents) {
      if (auth != null)
      dependent.setModifiedBy(auth.getName());
      dependent.setModifiedDate(new Date());
    }
    dependentRepository.saveAll(dependents);
  }

}
