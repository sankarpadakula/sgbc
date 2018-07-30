package com.sp.sgbc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.sgbc.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
 
  List<Transaction> findByApplicantId(Long applicantId);
}
