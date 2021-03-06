package com.sp.sgbc.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sp.sgbc.model.Transaction;
import com.sp.sgbc.repository.TransactionRepository;

@Service
public class TransactionService {

  @Autowired
  TransactionRepository transactionRepository;

  public List<Transaction> getAllTransaction(Long id) {
    return transactionRepository.findByApplicantId(id);
  }

  public Transaction save(Transaction transaction) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null)
      transaction.setModifiedBy(auth.getName());
    transaction.setModifiedDate(new Date());
    return transactionRepository.save(transaction);
  }

  public Transaction findOne(Long id) {
    return transactionRepository.findById(id).orElse(null);
  }

  public List<Transaction> getTransactionsByApplicantId(Long appId) {
    return transactionRepository.findByApplicantId(appId);
  }

  public boolean exists(Long id) {
    return transactionRepository.existsById(id);
  }

  public void save(List<Transaction> transactions) {
    transactionRepository.saveAll(transactions);
    
  }
}
