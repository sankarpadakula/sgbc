package com.sp.sgbc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.sgbc.model.Applicant;

@Repository("applicantRepository")
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
  Applicant findByEmail(String email);
  Applicant findByName(String name);
  Applicant findByConfirmationToken(String token);
  List<Applicant> findByActive(boolean active);
}
