package com.sp.sgbc.controller;

import static java.util.Collections.singletonList;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.model.DependentResponse;
import com.sp.sgbc.model.Transaction;
import com.sp.sgbc.model.TransactionResponse;
import com.sp.sgbc.model.ViewPage;
import com.sp.sgbc.service.ApplicantService;
import com.sp.sgbc.service.DependentService;
import com.sp.sgbc.service.SchedulerService;
import com.sp.sgbc.service.TransactionService;

@RestController
@RequestMapping("/rest")
public class ApplicantRestController {

  @Autowired
  private ApplicantService applicantService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private DependentService dependentService;
  
  @Autowired
  SchedulerService schedulerService;

  @RequestMapping(value = "/{id}", method = GET)
  public Applicant read(@PathVariable("id") Long id) {
    return applicantService.findOne(id);
  }

  @RequestMapping(method = GET)
  public ViewPage<Applicant> listApplicants(Pageable page) {
    Page<Applicant> applicants = applicantService.findAll(page);
    return new ViewPage<Applicant>(applicants);
  }

  @RequestMapping(value = "/{id}", method = PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateApplicant(@PathVariable("id") Long id, @RequestBody Applicant applicant) {
    if (applicantService.exists(id)) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      applicant.setId(id);
      applicant.setModifiedBy(auth.getName());
      applicant.setModifiedDate(new Date());
      applicantService.save(applicant);
      if (!applicant.isActive()) {
        List<Dependent> dependents = dependentService.getDependentsByApplicantId(id);
        for (Dependent dependent : dependents) {
          dependent.setModifiedBy(auth.getName());
          dependent.setModifiedDate(new Date());
          dependent.setActive(false);
        }
        dependentService.save(dependents);
      }
    }
  }

  @RequestMapping(method = POST)
  public ResponseEntity<String> createApplicant(HttpServletRequest request, @RequestBody Applicant applicant) {
    applicant = applicantService.save(applicant);

    URI uri = new UriTemplate("{requestUrl}/{id}").expand(request.getRequestURL().toString(), applicant.getId());
    final HttpHeaders headers = new HttpHeaders();
    headers.put("Location", singletonList(uri.toASCIIString()));
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void disableApplicant(@PathVariable("id") Long id) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Applicant applicant = applicantService.findOne(id);
    applicant.setModifiedBy(auth.getName());
    applicant.setModifiedDate(new Date());
    applicant.setActive(false);

    List<Dependent> dependents = dependentService.getDependentsByApplicantId(id);
    for (Dependent dependent : dependents) {
      dependent.setModifiedBy(auth.getName());
      dependent.setModifiedDate(new Date());
      dependent.setActive(false);
    }
    dependentService.save(dependents);
    applicantService.save(applicant);
  }

  /**
   * Dependents rest calls
   */
  @RequestMapping(value = "/dependents/{id}", method = { GET })
  public DependentResponse getDependantsByApplicantId(@PathVariable("id") Long id) {
    List<Dependent> dependents = dependentService.getDependentsByApplicantId(id);
    DependentResponse response = new DependentResponse();
    response.setRows(dependents);
    return response;
  }

  @RequestMapping(value = "/dependents/{id}", method = PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateDependent(@PathVariable("id") Long id, @RequestBody Dependent dependent) {
    if (dependentService.exists(id)) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      dependent.setModifiedBy(auth.getName());
      dependent.setModifiedDate(new Date());
      dependent.setId(id);
      dependentService.save(dependent);
    }
  }

  @RequestMapping(value = "/dependents/{id}", method = POST)
  public ResponseEntity<String> createDependent(HttpServletRequest request, @PathVariable("id") Long id,
      @RequestBody Dependent dependent) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    dependent.setModifiedBy(auth.getName());
    dependent.setModifiedDate(new Date());
    dependent.setApplicant(applicantService.findOne(id));
    dependent = dependentService.save(dependent);

    URI uri = new UriTemplate("{requestUrl}/{id}").expand(request.getRequestURL().toString(), dependent.getId());
    final HttpHeaders headers = new HttpHeaders();
    headers.put("Location", singletonList(uri.toASCIIString()));
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/dependents/{id}", method = DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void disableDependent(@PathVariable("id") Long id) {
    Dependent dependent = dependentService.findOne(id);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    dependent.setModifiedBy(auth.getName());
    dependent.setModifiedDate(new Date());
    dependent.setActive(false);
    dependentService.save(dependent);
  }

  /**
   * Transactions rest services
   * 
   */
  @RequestMapping(value = "/transactions/{id}", method = { GET })
  public TransactionResponse getTransactionByApplicantId(@PathVariable("id") Long id) {
    List<Transaction> trans = transactionService.getAllTransaction(id);
    double total = 0d;
    for (Transaction transaction : trans) {
      total += transaction.getAmountPaid();
    }
    TransactionResponse response = new TransactionResponse();
    response.setRows(trans);
    if (total != 0) {
      response.getUserdata().put("transactionDate", "Total");
      response.getUserdata().put("amountPaid", total + "");
    }
    return response;
  }

  @RequestMapping(value = "/transactions", method = { GET })
  public List<Transaction> getTransactions() {
    // return transactionService.getAllTransaction(id);
    return new ArrayList<Transaction>();
  }

  @RequestMapping(value = "/transactions/{id}", method = { POST })
  public void postTransaction(@PathVariable("id") Long id, @RequestBody Transaction transaction) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    transaction.setCreatedBy(auth.getName());
    transaction.setCreatedDate(new Date());
    transaction.setApplicantId(id);
    transactionService.save(transaction);
  }

  @RequestMapping(value = "/transactions/{id}", method = PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateTransaction(@PathVariable("id") Long id, @RequestBody Transaction transaction) {
    if (transactionService.exists(id)) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      transaction.setModifiedBy(auth.getName());
      transaction.setModifiedDate(new Date());
      transaction.setId(id);
      transactionService.save(transaction);
    }
  }

  @RequestMapping(value = "/transactions/{id}", method = DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void disableTransaction(@PathVariable("id") Long id) {
    Transaction transaction = transactionService.findOne(id);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    transaction.setModifiedBy(auth.getName());
    transaction.setModifiedDate(new Date());
    transaction.setActive(false);
    transactionService.save(transaction);
  }

  @RequestMapping(value = "/processTransactionFile", method = GET)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void processTransactionFile() {
    schedulerService.processTransactionFile();
  }
  
  /*
  private Page<Applicant> build() {
    List<Applicant> list = new ArrayList<>();
    for (int i = 1; i < 11; i++) {
      list.add(build(i));
    }
    return new PageImpl(list);
  }

  private Applicant build(long i) {
    Applicant applicant = new Applicant();
    applicant.setId(11l + i);
    applicant.setName("griduser" + i);
    applicant.setEmail("sankar@gmail.com" + i);
    applicant.setPhone("12345" + i);
    applicant.setMaritalStatus("Single");
    applicant.setStartDate(new Date());
    applicant.setInitBalance(1000.22 * i);
    applicant.setCloseBalance(900 * i);
    applicant.setActive(false);
    applicant.setChildrens(buildDeps(i));
    return applicant;
  }

  private Transaction buildTrans(long i) {
    Transaction applicant = new Transaction();
    applicant.setId(11l + i);
    applicant.setApplicantId(11l + i);
    applicant.setTransactionId("trans" + i);
    applicant.setAmountPaid(10d + i);
    applicant.setTransactionDate(new Date());
    return applicant;
  }

  private List<Dependent> buildDeps(Long id) {
    List<Dependent> trans = new ArrayList<Dependent>();
    for (int i = 0; i <= 4; i++) {
      trans.add(buildDep(id + i));
    }
    return trans;
  }

  private Dependent buildDep(long i) {
    Dependent applicant = new Dependent();
    applicant.setId(11l + i);
    applicant.setName("dep" + i);
    applicant.setRemainder(false);
    applicant.setActive(true);
    applicant.setDateOfBirth(new Date());
    applicant.setGender("M");
    return applicant;
  }*/
}
