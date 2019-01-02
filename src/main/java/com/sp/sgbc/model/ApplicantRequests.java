package com.sp.sgbc.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
public class ApplicantRequests {

  @Id
  private String uid;

  @Lob
  @Type(type = "org.hibernate.type.TextType")  
  private String data;

  public ApplicantRequests() {

  }

  public ApplicantRequests(String uid, Applicant applicant) {
    try {
      this.data = new ObjectMapper().writeValueAsString(applicant);
    } catch (JsonProcessingException e) {
    }
    this.uid = uid;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

}
