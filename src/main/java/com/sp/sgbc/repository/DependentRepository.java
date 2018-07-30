package com.sp.sgbc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sp.sgbc.model.Dependent;
import com.sp.sgbc.util.DependentType;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, Long>{
 
  @Query("from Dependent where active = true and remainder = true and applicant is not null and type = :type and MONTHS_BETWEEN(SYSDATE, dateOfBirth) >= :monthsFrom")
  List<Dependent> findByAgeMorethanMonthsRemainded(@Param("type") DependentType type, @Param("monthsFrom") int monthsFrom);
  
  @Query("from Dependent where active = true and remainder = false and applicant is not null and type = :type and MONTHS_BETWEEN(SYSDATE, dateOfBirth) >= :monthsFrom")
  List<Dependent> findByAgeMorethanMonths(@Param("type") DependentType type, @Param("monthsFrom") int monthsFrom);
  
  @Query("from Dependent where applicant.id = :appId")
  List<Dependent> findByApplicantId(@Param("appId") Long appId);
}
