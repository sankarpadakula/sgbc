package com.sp.sgbc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "applicant")
public class Applicant {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @ManyToOne(cascade = CascadeType.ALL)
  private Address address;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  private String phone;

  private String gender;

  private String placeOfBirth;

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
  private List<Dependent> childrens = new ArrayList<Dependent>();

  private String notes;

  private String wishes;

  private boolean active;

  private transient MultipartFile docs;

  private String fileName;

  @Lob
  private byte[] fileData;

  private String confirmationToken;

  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private Date startDate;

  private String createdBy;

  @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
  @Temporal(TemporalType.DATE)
  private Date createdDate;

  private String modifiedBy;

  @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
  @Temporal(TemporalType.DATE)
  private Date modifiedDate;

  private transient double initBalance;
  private transient double closeBalance;

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

  public String getConfirmationToken() {
    return confirmationToken;
  }

  public void setConfirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
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

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public byte[] getFileData() {
    return fileData;
  }

  public void setFileData(byte[] fileData) {
    this.fileData = fileData;
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

}