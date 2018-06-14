package com.sp.sgbc.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

  private TemplateEngine templateEngine;
  
  @Autowired
  public MailContentBuilder(TemplateEngine templateEngine) {
      this.templateEngine = templateEngine;
  }

  public String build(String template, Map<String, Object> messagemap) {
      Context context = new Context();
      context.setVariable("message", messagemap.get("url"));
      context.setVariables(messagemap);;
      return templateEngine.process("email/"+template, context);
  }
}
