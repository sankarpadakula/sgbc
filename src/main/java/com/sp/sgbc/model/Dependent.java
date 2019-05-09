package com.sp.sgbc.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sp.sgbc.util.DependentType;

@Entity
@Table(name = "dependent")
@JsonIgnoreProperties(value = { "docs" })
public class Dependent {
  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="dependent")
  @TableGenerator(name="dependent", table="SEQ_GENERATOR", pkColumnName = "key", valueColumnName = "next", 
  pkColumnValue="dependent",allocationSize=1)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JsonBackReference
  private Applicant applicant;

  private String name;

  private String placeOfBirth;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  private String bsnNum;
  
  private DependentType type;

  private String gender;
  
  private String phone;

  @Transient
  private MultipartFile docs;
  
  private String fileName;
  
  private Boolean active;

  private boolean remainder;

  private String createdBy;

  @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
  @Temporal(TemporalType.DATE)
  private Date createdDate;

  private String modifiedBy;

  @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
  @Temporal(TemporalType.DATE)
  private Date modifiedDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Applicant getApplicant() {
    return applicant;
  }

  public void setApplicant(Applicant applicant) {
    this.applicant = applicant;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPlaceOfBirth() {
    return placeOfBirth;
  }

  public void setPlaceOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getBsnNum() {
    return bsnNum;
  }

  public void setBsnNum(String bsnNum) {
    this.bsnNum = bsnNum;
  }

  public boolean isRemainder() {
    return remainder;
  }

  public void setRemainder(boolean remainder) {
    this.remainder = remainder;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public DependentType getType() {
    return type;
  }

  public void setType(DependentType type) {
    this.type = type;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public MultipartFile getDocs() {
    return docs;
  }

  public void setDocs(MultipartFile docs) {
    this.docs = docs;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

}
