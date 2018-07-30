package com.sp.sgbc.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.model.Transaction;
import com.sp.sgbc.repository.DependentRepository;
import com.sp.sgbc.util.DependentType;
import com.sp.sgbc.util.Helper;

@Service
public class SchedulerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

  @Autowired
  DependentRepository dependentRepository;

  @Autowired
  ApplicantService applicantService;

  @Autowired
  TransactionService transactionService;

  @Autowired
  EmailService emailService;

  @Autowired
  MailContentBuilder mailContent;

  @Autowired
  private MessageSource messageSource;

  @Value("${transaction.csvfile.path}")
  private String filePath;

  static final String DEFAULT_EMAIL_AGE_REM = "Letter for children 18years";
  static final String DEFAULT_EMAIL_UNPAID_REM = "Letter for un-paid";

  static final String NON_PAID_APPLICANT_WARN = "nonPaidWarningToApplicant";
  static final String NON_PAID_APPLICANT_REM = "nonPaidReminderToApplicant";
  static final String NON_PAID_APPLICANT_ADMIN = "nonPaidReminderToAdmin";
  static final String AGE_APPLICANT_REM = "ageReminderToApplicant";
  static final String AGE_APPLICANT_ADMIN = "ageReminderToAdmin";

  public void notify18YearsAgeRemainder() {
    LOGGER.info("18YearsAgeRemainder process started " + new Date());
    List<Dependent> dependents = dependentRepository.findByAgeMorethanMonths(DependentType.Child, 213);
    sendAgeRemainderemails(dependents, false);
  }

  public void notify18YearsAgeRemainderAndInactive() {
    LOGGER.info("18YearsAgeRemainder process started " + new Date());
    List<Dependent> dependents = dependentRepository.findByAgeMorethanMonthsRemainded(DependentType.Child, 216);
    sendAgeRemainderemails(dependents, true);
  }

  void sendAgeRemainderemails(List<Dependent> dependents, boolean inActiveRequired) {
    String subject = messageSource.getMessage("age.reachingEighteen.subject", null, DEFAULT_EMAIL_AGE_REM,
        LocaleContextHolder.getLocale());
    Map<String, Object> map = null;
    if (!dependents.isEmpty()) {
      for (Dependent dependent : dependents) {
        String parentEmail = dependent.getApplicant().getEmail();
        map = new HashMap<String, Object>();
        map.put("dependent", dependent);
        String body = mailContent.build(AGE_APPLICANT_REM, map);
        emailService.sendHtmlEmail(subject, body, true, parentEmail);
        dependent.setRemainder(true);
        if (inActiveRequired) {
          dependent.setActive(false);
        }
      }
      map = new HashMap<String, Object>();
      map.put("dependents", dependents);
      String body = mailContent.build(AGE_APPLICANT_ADMIN, map);
      emailService.sendHtmlEmail(subject, body);
      dependentRepository.saveAll(dependents);
    }

  }

  public void processTransactionFile() {
    LOGGER.info("Transaction CSV File process started " + new Date());
    File sourceDir = new File(filePath);
    File[] files = sourceDir.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".csv") || name.toLowerCase().endsWith(".xls")
            || name.toLowerCase().endsWith(".xlsx");
      }
    });
    if (files == null || files.length == 0) {
      emailService.sendEmail("No File to Process", "Please place a file and process through application");
    }
    for (File file : files) {
      try {
        List<Transaction> transactions = null;
        if (file.getName().toLowerCase().endsWith(".csv")) {
          transactions = Helper.readCSV(file.getAbsolutePath());
        } else {
          transactions = Helper.readExcel(file.getAbsolutePath());
        }
        if (transactions != null && !transactions.isEmpty()) {
          transactionService.save(transactions);
        }
        Helper.archive(file);
      } catch (Exception e) {
        emailService.sendEmail("File Process failed " + file.getName(), e.getMessage());
      }
    }
  }

  public void notifyUnpaidApplicants(int months) {
    LOGGER.info("UnpaidApplicants process started " + new Date());

    List<Applicant> applicants = applicantService.getUnpaidApplicants(months);
    String subject = messageSource.getMessage("unPaid.applicant.subject", null, DEFAULT_EMAIL_UNPAID_REM,
        LocaleContextHolder.getLocale());
    Map<String, Object> map = null;
    for (Applicant applicant : applicants) {
      applicant.setActive(false);
      map = new HashMap<String, Object>();
      map.put("applicant", applicant);
      String body = mailContent.build(months == 3 ? NON_PAID_APPLICANT_REM : NON_PAID_APPLICANT_WARN, map);
      emailService.sendHtmlEmail(subject, body, true, applicant.getEmail());
    }
    map = new HashMap<String, Object>();
    map.put("applicants", applicants);
    map.put("months", months);
    String body = mailContent.build(NON_PAID_APPLICANT_ADMIN, map);
    emailService.sendHtmlEmail(subject, body);
    if (months > 5) {
      applicantService.save(applicants);
    }
  }

}
