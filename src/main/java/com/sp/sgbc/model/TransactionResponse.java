package com.sp.sgbc.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionResponse {

  List<Transaction> rows;
  
  Map<String, String> userdata = new HashMap<String, String>();

  public List<Transaction> getRows() {
    return rows;
  }

  public void setRows(List<Transaction> transactions) {
    this.rows = transactions;
  }

  public Map<String, String> getUserdata() {
    return userdata;
  }

  public void setUserdata(Map<String, String> userData) {
    this.userdata = userData;
  }
  
  
}
