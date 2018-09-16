package com.sp.sgbc.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class MailContentBuilder {

  private TemplateEngine templateEngine;

  DateFormat dateformat = new SimpleDateFormat("dd MMM yyyy");
  @Autowired
  public MailContentBuilder(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public String build(String template, Map<String, Object> messagemap) {
    Context context = new Context();
    context.setVariable("date", dateformat.format(new Date()));
    context.setVariables(messagemap);
    return templateEngine.process("email/" + template, context);
  }

  public String includeTemplate(String body) {
    Context context = new Context();
    context.setVariable("date", dateformat.format(new Date()));
    context.setVariable("body", body);
    return templateEngine.process("email/pdfTemplate", context);
  }
}
