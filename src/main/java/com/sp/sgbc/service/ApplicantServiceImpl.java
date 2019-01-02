package com.sp.sgbc.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.model.ApplicantRequests;
import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.model.Transaction;
import com.sp.sgbc.repository.ApplicantRepository;
import com.sp.sgbc.repository.ApplicantRequestRepository;
import com.sp.sgbc.util.Helper;

@Service("applicantService")
public class ApplicantServiceImpl implements ApplicantService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicantServiceImpl.class);

  @Autowired
  private ApplicantRepository applicantRepository;

  @Autowired
  private ApplicantRequestRepository applicantRequestRepository;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private FileStorageService fileStorageService;
  
  @Value("${registration.pay.person}")
  private Integer payPerPerson;

  public Applicant findApplicantByEmail(String email) {
    return applicantRepository.findByEmail(email);
  }

  public Applicant findApplicantByBsnNum(String bsnNum) {
    return applicantRepository.findByBsnNum(bsnNum);
  }

  @Transactional
  public Applicant save(Applicant applicant) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null)
      applicant.setModifiedBy(auth.getName());
    applicant.setModifiedDate(new Date());
    if (applicant.getPartner() != null) {
      applicant.getPartner().setActive(applicant.isActive());
    }
    if (applicant.getOtherContact() != null) {
      applicant.getOtherContact().setActive(applicant.isActive());
    }
    if (applicant.getChildrens() != null) {
      for (Dependent dependent : applicant.getChildrens()) {
        dependent.setActive(applicant.isActive());
        dependent.setRemainder(false);
      }
    }
    /*try {
      convertMultiMapToFile(applicant);
    } catch (IOException e) {
    }*/
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
      if (applicant.getStartDate() != null)
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

  public Applicant findByUid(String uid) {
    ApplicantRequests request = applicantRequestRepository.findByUid(uid);
    if (request != null) {
      try {
        return new ObjectMapper().readValue(request.getData(), Applicant.class);
      } catch (IOException e) {
        LOGGER.error("Converting to object failed", e);
      }
    }
    return null;
  }

  private void storeAndConvertMultiMapToFile(Applicant applicant) throws IOException {
    MultipartFile file = applicant.getDocs();
    if (file != null && !file.getOriginalFilename().isEmpty()) {
      applicant.setFileName(file.getOriginalFilename());
      fileStorageService.storeFile(applicant.getBsnNum(),file);
    }
    if (applicant.getPartner() != null && applicant.getPartner().getDocs() != null && !applicant.getPartner().getDocs().getOriginalFilename().isEmpty()) {
      file = applicant.getPartner().getDocs();
      applicant.getPartner().setFileName(file.getOriginalFilename());
      fileStorageService.storeFile(applicant.getPartner().getBsnNum(),file);
    }
    if (applicant.getChildrens() != null) {
      for (Dependent kid : applicant.getChildrens()) {
        if (kid.getDocs() != null && !kid.getDocs().getOriginalFilename().isEmpty()) {
          file = kid.getDocs();
          kid.setFileName(file.getOriginalFilename());
          fileStorageService.storeFile(kid.getBsnNum(),file);
        }
      }
    }
  }

  public String save(String uid, Applicant applicant) {
    try {
      
      /*try {
        convertMultiMapToFile(applicant);
        Applicant oldRequest = findByUid(uid);
        if (oldRequest != null) {
          if (applicant.getFileData() == null || applicant.getFileData().length ==0) {
            applicant.setFileData(oldRequest.getFileData());
          }
          if (applicant.getPartner() != null && oldRequest.getPartner() != null && 
              (applicant.getPartner().getFileData() == null || applicant.getPartner().getFileData().length ==0)) {
            applicant.getPartner().setFileData(oldRequest.getPartner().getFileData());
          }
          if (applicant.getChildrens() != null) {
            for (Dependent kid : applicant.getChildrens()) {
              if (kid.getFileData() == null || kid.getFileData().length ==0) {
                for (Dependent oldKid : oldRequest.getChildrens()) {
                  if (oldKid.getFileData().length!=0 && oldKid.getBsnNum().equals(kid.getBsnNum()))
                    kid.setFileData(oldKid.getFileData());
                }
              }
            }
          }
        }
      } catch (IOException e) {
        LOGGER.error("MultiMapToFile failed", e);
      }*/
      if (uid == null || uid.isEmpty())
        uid = UUID.randomUUID().toString();
      storeAndConvertMultiMapToFile(applicant);
      ApplicantRequests request = new ApplicantRequests(uid, applicant);
      request = applicantRequestRepository.save(request);
      return request.getUid();
    } catch (Exception e) {
      LOGGER.error("Parsing failed", e);
    }
    return "";
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

  @Transactional(value = TxType.REQUIRES_NEW)
  public void deleteByUid(String token) {
    applicantRequestRepository.deleteByUid(token);

  }
}
