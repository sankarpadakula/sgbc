package com.sp.sgbc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sp.sgbc.model.Applicant;
import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.service.ApplicantService;
import com.sp.sgbc.service.EmailService;
import com.sp.sgbc.service.FileStorageService;
import com.sp.sgbc.service.MailContentBuilder;
import com.sp.sgbc.util.DependentType;
import com.sp.sgbc.util.Helper;
import com.sp.sgbc.validator.ApplicantValidator;

@Controller
public class RegisterController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  @Autowired
  private ApplicantService applicantService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  MailContentBuilder mailContent;

  @Autowired
  ApplicantValidator applicantValidator;

  @Autowired
  FileStorageService fileStorageService;

  @Value("${registration.pay.person}")
  private Integer payPerPerson;

  private final String REGISTRATION_PAGE = "registration";
  private final String TERMS_PAGE = "terms-and-conditions";
  private final String ADMIN_PAGE = "admin/home";

  private final String VALIDATION_ERROR = "validationMessage";
  private final String INFORMATION_MESSAGE = "confirmationMessage";
  static final String DEFAULT_DUPLICATE_USER = "Oops!  There is already a user registered with the email provided.";
  static final String DEFAULT_INVALID_TOKEN = "Oops!  This is an invalid confirmation link.";
  static final String DEFAULT_APP_SUBMIT_CONFIRM = "Registration Request send to admin. Once approved you will receive a registration number";
  static final String DEFAULT_EMAIL_APP_REQ = "Registration Request";
  static final String DEFAULT_EMAIL_APP_RES = "Confirm Registration";

  // Return registration form template
  @RequestMapping(value = "/", method = GET)
  public ModelAndView showRegistrationPage(ModelAndView modelAndView, Applicant applicant) {
    modelAndView.addObject("user", applicant);
    modelAndView.setViewName(REGISTRATION_PAGE);
    return modelAndView;
  }

  @RequestMapping(value = "/{id}", method = GET)
  public ModelAndView showApplicantPage(ModelAndView modelAndView, @PathVariable("id") Long id) {
    Applicant applicant = applicantService.findOne(id);
    modelAndView.addObject("user", applicant);
    modelAndView.setViewName(REGISTRATION_PAGE);
    return modelAndView;
  }

  // Process form input data
  @RequestMapping(value = "/", method = POST)
  public ModelAndView processRegistrationForm(ModelAndView modelAndView, Applicant user,
      @RequestParam("docs") List<MultipartFile> files, BindingResult bindingResult, HttpServletRequest request) {
    Locale locale = LocaleContextHolder.getLocale();
    String error = validate(user, bindingResult);
    if (error != null) {
      modelAndView.addObject(VALIDATION_ERROR, error);
    } else {
      buildNewApplicantDefaults(user);
      String token = applicantService.save(user.getToken(), user);
      String appUrl = request.getRequestURL().toString() + "confirm?token=" + token;
      // appUrl = request.getScheme() + "://" + request.getServerName() + request.getServerPort() +

      LOGGER.info("To Activate " + appUrl);
      String subject = messageSource.getMessage("registration.request.subject", null, DEFAULT_EMAIL_APP_REQ, locale);
      String body = buildApprovalRequestMessage(appUrl, user);
      emailService.sendHtmlEmail(subject, body);

      user = new Applicant();
      String message = messageSource.getMessage("registration.submission.confirmation", null,
          DEFAULT_APP_SUBMIT_CONFIRM, locale);
      modelAndView.addObject(INFORMATION_MESSAGE, message);
    }
    modelAndView.addObject("user", user);
    modelAndView.setViewName(REGISTRATION_PAGE);
    return modelAndView;
  }

  private String validate(@Valid Applicant user, BindingResult bindingResult) {
    applicantValidator.validate(user, bindingResult);
    if (bindingResult.hasErrors()) {
      // String error = bindingResult.getAllErrors().stream().map(v ->
      // v.toString().split(";")[0]).collect(Collectors.joining(","));
      String error =  getError(bindingResult);
      LOGGER.warn("Validation failed " + error);
      return error;
    }
    return null;
  }

  @RequestMapping(value = "/confirm", method = GET)
  public ModelAndView confirmRegistrationRequest(ModelAndView modelAndView,
      @RequestParam(name = "token", required = false) String token,
      @RequestParam(name = "id", required = false) Long regid) {
    Applicant user = null;
    if (regid == null)
      user = applicantService.findByUid(token);
    else
      user = applicantService.findOne(regid);

    if (user == null) { // No token found in DB
      user = new Applicant();
      String message = messageSource.getMessage("registration.invalid.token", null, DEFAULT_INVALID_TOKEN,
          LocaleContextHolder.getLocale());
      modelAndView.addObject(VALIDATION_ERROR, message);
    } else { // Token found
      modelAndView.addObject("confirmationToken", token);
      user.setToken(token);
    }
    modelAndView.addObject("user", user);
    modelAndView.setViewName(REGISTRATION_PAGE);
    return modelAndView;
  }

  @GetMapping("/files/{uid}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String uid,
      @RequestParam(name = "file", required = false) String filename,
      @RequestParam(name = "bsnNum", required = false) String bsnNum, HttpServletRequest request) throws Exception {
    Resource resource = fileStorageService.loadFileAsResource(bsnNum + Helper.getFileExtension(filename));
    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (Exception ex) {
      LOGGER.info("Could not determine file type." + ex.getMessage());
    }

    // Fallback to the default content type if type could not be determined
    if (contentType == null) {
      contentType = "application/octet-stream";
    }
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").body(resource);
  }

  public static boolean isNumeric(String str) {
    return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
  }

  @DeleteMapping("/files/{uid}")
  public void removeFile(@PathVariable String uid, @RequestParam(name = "file", required = false) String filename,
      @RequestParam(name = "bsnNum", required = false) String bsnNum) {
    Applicant doc = applicantService.findByUid(uid);
    /*
     * if ("parent".equals(type)) { doc.setFileName(null); doc.setDocs(null); } else if ("partner".equals(type)) {
     * Dependent dependent = doc.getPartner(); dependent.setFileName(null); dependent.setFileData(null);
     * dependent.setDocs(null); } else { List<Dependent> dependents = doc.getChildrens(); if (dependents != null) for
     * (Dependent dependent : dependents) { if (filename.equals(dependent.getFileName())) { dependent.setFileName(null);
     * dependent.setFileData(null); dependent.setDocs(null); break; } } }
     */
    applicantService.save(uid, doc);
  }

  @RequestMapping(value = "/admin/home", method = { RequestMethod.POST, GET })
  public ModelAndView confirmRegistration(ModelAndView modelAndView, Applicant user,
      @RequestParam(name = "token", required = false) String token, BindingResult bindingResult, HttpServletRequest request) {
    String body = null;
    String subject = messageSource.getMessage("registration.confirmation.subject", null, DEFAULT_EMAIL_APP_RES,
        LocaleContextHolder.getLocale());
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Applicant applicant = null;
    if (token != null && !token.isEmpty())
      applicant = applicantService.findByUid(token);
    else if (auth != null && user.isActive()) {
      /*String error = validate(user, bindingResult);
      if (error != null) {
        modelAndView.addObject("user", user);
        modelAndView.addObject(VALIDATION_ERROR, error);
        modelAndView.setViewName(REGISTRATION_PAGE);
        return modelAndView;
      }*/
      buildNewApplicantDefaults(user);
      applicant = user;
    }
    
    if (applicant != null) {
      applicant.setActive(user.isActive());
      applicant.setNotes(user.getNotes());
      if (applicant.isActive()) {
        applicant.setStartDate(Helper.getNextMonthStartDate());
        applicantService.save(applicant);
        if (token != null && !token.isEmpty())
          applicantService.deleteByUid(token);
        int noOfPersons = 1;
        if (applicant.getPartner() != null) {
          noOfPersons = noOfPersons + 1;
        }
        body = buildApprovalConfirmationMessage(payPerPerson * noOfPersons, applicant);
      } else {
        applicantService.save(token, user);
        String appUrl = request.getRequestURL().toString().replaceAll(request.getServletPath(), "") + "/confirm?token="
            + token;
        body = buildRejectRequestMessage(appUrl, applicant);
      }
      emailService.sendHtmlEmail(subject, body, true, new String[] { user.getEmail() });
    }
    
    modelAndView = new ModelAndView();
    modelAndView.setViewName(ADMIN_PAGE);
    return modelAndView;
  }

  @RequestMapping(value = "/terms-and-conditions", method = GET)
  public ModelAndView termsandConditions(ModelAndView modelAndView) {
    modelAndView.setViewName(TERMS_PAGE);
    return modelAndView;
  }

  private void buildNewApplicantDefaults(Applicant user) {
    if (user.getPartner() == null || user.getPartner().getName() == null || user.getPartner().getName().isEmpty()) {
      user.setPartner(null);
    } else {
      user.getPartner().setType(DependentType.Spouse);
    }
    if (user.getOtherContact() == null || user.getOtherContact().getName() == null
        || user.getOtherContact().getName().isEmpty()) {
      user.setOtherContact(null);
      user.setOtherContactAddress(null);
    } else {
      user.getOtherContact().setType(DependentType.OtherContact);
    }
    if (user.getChildrens() != null) {
      for (Dependent kid : user.getChildrens()) {
        kid.setType(DependentType.Child);
        kid.setApplicant(user);
      }
    }
    user.setActive(false);
    user.setCreatedDate(new Date());
  }

  private String getError(BindingResult bindingResult) {
    StringBuilder b = new StringBuilder();
    b.append(bindingResult.getGlobalErrors().stream().map(v -> v.getCode()).collect(Collectors.joining("; \n")));
    if (b.length() > 0)
      b.append("; \n");
    b.append(bindingResult.getFieldErrors().stream().map(v -> v.getField().split("\\.")[0] + ":" + v.getCode()).distinct()
        .collect(Collectors.joining("; \n")));
    return b.toString().replaceAll("\\.", "'s ").replaceAll("\\[0\\]", "");
  }

  private String buildApprovalRequestMessage(String baseurl, Applicant user) {
    int dependents = user.getPartner() != null ? 1 : 0;
    if (user.getChildrens() != null && !user.getChildrens().isEmpty()) {
      dependents += user.getChildrens().size();
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("applicant", user.getName());
    map.put("dependents", dependents);
    map.put("url", baseurl);
    // StringBuffer info = new StringBuffer(String.format("Customer Submission: name = %s, bsn Num = %s, dependends =
    // %s\n", user.getName(), user.getBsnNum(), dependents));
    String info = mailContent.build("registrationReq", map);
    return info;
  }

  private String buildRejectRequestMessage(String appurl, Applicant applicant) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("applicant", applicant);
    map.put("link", appurl);
    return mailContent.build("registrationRejection", map);
  }

  private String buildApprovalConfirmationMessage(int pay, Applicant user) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("applicant", user);
    map.put("pay", pay);
    return mailContent.build("registrationConfirm", map);
  }

}