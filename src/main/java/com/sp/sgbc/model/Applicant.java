package com.sp.sgbc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sp.sgbc.configuration.CustomJsonDateSerializer;

@Entity
@Table(name = "applicant")
@JsonIgnoreProperties(value = { "docs" })
public class Applicant implements Serializable{

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="applicant")
  @TableGenerator(name="applicant", table="SEQ_GENERATOR", pkColumnName = "key", valueColumnName = "next", 
  pkColumnValue="applicant",allocationSize=1, initialValue=1100000)
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @ManyToOne(cascade = CascadeType.ALL)
  private Address address;

  @Column(name = "email", nullable = false)
  private String email;

  private String phone;

  private String gender;

  private String placeOfBirth;

  @JsonDeserialize(using = CustomJsonDateSerializer.class)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  private String maritalStatus;

  private String bsnNum;

  private Boolean healthy;

  @OneToOne(cascade = CascadeType.ALL)
  private Dependent partner;

  @OneToOne(cascade = CascadeType.ALL)
  private Dependent otherContact;

  @ManyToOne(cascade = CascadeType.ALL)
  private Address otherContactAddress;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "applicant")
  @JsonManagedReference
  private List<Dependent> childrens = new ArrayList<Dependent>();

  private String notes;

  private String wishes;

  @Column(nullable = false,columnDefinition = "boolean default true") 
  private boolean active;

  @Transient
  private MultipartFile docs;

  private String fileName;

  @DateTimeFormat(pattern = "dd/MM/yyyy")
  @Temporal(TemporalType.DATE)
  private Date startDate;

  private String createdBy;

  @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
  @Temporal(TemporalType.DATE)
  @Column(nullable = true)
  private Date createdDate;

  private String modifiedBy;

  @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
  @Temporal(TemporalType.DATE)
  @Column(nullable = true)
  private Date modifiedDate;

  @Transient
  private double initBalance;
  @Transient
  private double closeBalance;
  @Transient
  private transient String formatedId;
  @Transient
  private transient String token;
  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getFormatedId() {
    if (id != null) {
      String num = Long.toString(id);
      if (num.length() > 3)
        return num.substring(0, 2) + "-" + num.substring(num.length() - 2, num.length());
    }
    return id + "";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public MultipartFile getDocs() {
    return docs;
  }

  public void setDocs(MultipartFile docs) {
    this.docs = docs;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getPlaceOfBirth() {
    return placeOfBirth;
  }

  public void setPlaceOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public String getBsnNum() {
    return bsnNum;
  }

  public void setBsnNum(String bsnNum) {
    this.bsnNum = bsnNum;
  }

  public Boolean getHealthy() {
    return healthy;
  }

  public void setHealthy(Boolean healthy) {
    this.healthy = healthy;
  }

  public Dependent getPartner() {
    return partner;
  }

  public void setPartner(Dependent partner) {
    this.partner = partner;
  }

  public List<Dependent> getChildrens() {
    /*
     * if (childrens.isEmpty()) childrens.add(new Dependant());
     */
    return childrens;
  }

  public void setChildrens(List<Dependent> childrens) {
    this.childrens = childrens;
  }

  public Address getOtherContactAddress() {
    return otherContactAddress;
  }

  public void setOtherContactAddress(Address otherContactAddress) {
    this.otherContactAddress = otherContactAddress;
  }

  public Dependent getOtherContact() {
    return otherContact;
  }

  public void setOtherContact(Dependent otherContact) {
    this.otherContact = otherContact;
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

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getWishes() {
    return wishes;
  }

  public void setWishes(String wishes) {
    this.wishes = wishes;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public double getInitBalance() {
    return initBalance;
  }

  public void setInitBalance(double initBalance) {
    this.initBalance = initBalance;
  }

  public double getCloseBalance() {
    return closeBalance;
  }

  public void setCloseBalance(double closeBalance) {
    this.closeBalance = closeBalance;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}