package com.sp.sgbc.service;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sp.sgbc.util.Helper;

@Service("emailService")
public class EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  private JavaMailSender mailSender;

  @Autowired
  MailContentBuilder mailContent;

  @Value("${mail.from}")
  private String fromEmail;

  @Value("${mail.admin.approve}")
  private String adminEmails;

  @Autowired
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Async
  public void sendEmail(MimeMessage email) {
    mailSender.send(email);
  }

  @Async
  public void sendEmail(MimeMessagePreparator email) {
     //mailSender.send(email);
  }

  public void sendHtmlEmail(final String subject, final String body) {
    sendHtmlEmail(subject, body, false, new String[] {});
  }

  public void sendHtmlEmail(final String subject, final String body, final String... to) {
    sendHtmlEmail(subject, body, false, to);
  }

  public void sendEmail(final String subject, final String body) {
    sendEmail(subject, body, new String[] {});
  }
      
  public void sendEmail(final String subject, final String body, final String... to) {
    MimeMessagePreparator preparator = new MimeMessagePreparator() {
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        if (to == null || to.length == 0) {
          message.setTo(adminEmails.split("[\\s,;]+"));
        } else {
          message.setTo(to);
        }
        message.setSubject(subject);
        message.setFrom(fromEmail);
        message.setText(body, false);
      }
    };
    mailSender.send(preparator);
  }

  public void sendHtmlEmail(final String subject, final String body, final boolean attachRequired, final String... to) {
    MimeMessagePreparator registrationEmail = mimeMessage -> {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
      if (to == null || to.length == 0) {
        helper.setTo(adminEmails.split("[\\s,;]+"));
      } else {
        helper.setTo(to);
      }
      helper.setSubject(subject);
      helper.setText(body, true);
      if (attachRequired) {
        String attach = mailContent.includeTemplate(body);
        helper.addAttachment(subject + ".pdf", new ByteArrayResource(Helper.createPdf(attach)));
      }
      helper.setFrom(fromEmail);
    };
    sendEmail(registrationEmail);
    LOGGER.info(subject + " information sent to " + to);
  }
}
