package com.sp.sgbc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.sgbc.model.ApplicantRequests;

@Repository("applicantRequestRepository")
public interface ApplicantRequestRepository extends JpaRepository<ApplicantRequests, String> {

  ApplicantRequests findByUid(String uid);

  void deleteByUid(String token);
  
}
