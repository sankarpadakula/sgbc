package com.sp.sgbc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.model.Transaction;
import com.sp.sgbc.repository.ApplicantRepository;
import com.sp.sgbc.util.Helper;

@Service("applicantService")
public class ApplicantServiceImpl implements ApplicantService {

  @Autowired
  private ApplicantRepository applicantRepository;

  @Autowired
  private TransactionService transactionService;

  @Value("${registration.pay.person}")
  private Integer payPerPerson;

  public Applicant findApplicantByEmail(String email) {
    return applicantRepository.findByEmail(email);
  }

  public Applicant save(Applicant applicant) {
    return applicantRepository.save(applicant);
  }

  public Applicant findOne(Long id) {
    return applicantRepository.findById(id).orElse(null);
  }

  @Override
  public Applicant getApplicantByName(String name) {
    return applicantRepository.findByName(name);
  }

  @Override
  public List<Applicant> findAll() {
    return applicantRepository.findAll();
  }

  @Override
  public Page<Applicant> findAll(Pageable pageable) {
    Page<Applicant> applicants = applicantRepository.findAll(pageable);
    for (Applicant applicant : applicants) {
      calculateDueAndBuild(applicant);
    }
    return applicants;
  }

  private double calculateDueAndBuild(Applicant applicant) {
    List<Transaction> transactions = transactionService.getTransactionsByApplicantId(applicant.getId());
    double closeBal = 0d;
    for (Transaction transaction : transactions) {
      closeBal += transaction.getAmountPaid();
    }
    applicant.setCloseBalance(closeBal);

    double initBal = payPerPerson;
    if ((applicant.getPartner() != null && applicant.getPartner().getActive())
        || haveActiveChildrens(applicant.getChildrens())) {
      initBal = payPerPerson * 2;
    }
    int diffMonth = Helper.monthsDiffFromCurrent(applicant.getStartDate());
    double initBalance = diffMonth * initBal;
    applicant.setInitBalance(initBalance);

    return (initBalance - closeBal) / initBal;
  }

  @Override
  public Applicant findApplicantByConfirmationToken(String token) {
    return applicantRepository.findByConfirmationToken(token);
  }

  @Override
  public boolean exists(Long id) {
    return applicantRepository.existsById(id);
  }

  @Override
  public List<Applicant> getUnpaidApplicants(int months) {
    List<Applicant> applicants = applicantRepository.findByActive(true);
    List<Applicant> dueApplicants = new ArrayList<Applicant>();
    for (Applicant applicant : applicants) {
      if (calculateDueAndBuild(applicant) < -months) {
        dueApplicants.add(applicant);
      }
    }
    return dueApplicants;
  }

  private boolean haveActiveChildrens(List<Dependent> dependents) {
    if (dependents != null) {
      for (Dependent dependent : dependents) {
        if (dependent.getActive())
          return true;
      }
    }
    return false;
  }

  @Override
  public void save(List<Applicant> applicants) {
    applicantRepository.saveAll(applicants);
  }
}
