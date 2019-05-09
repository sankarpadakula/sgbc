package com.sp.sgbc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "address")
public class Address {

  @Id
  @GeneratedValue(strategy=GenerationType.TABLE, generator="address")
  @TableGenerator(name="address", table="SEQ_GENERATOR", pkColumnName = "key", valueColumnName = "next", 
  pkColumnValue="address",allocationSize=1)
  @Column(name = "id")
  private Long id;

  private String street;

  private String city;

  private String state;

  private String postcode;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

}