package com.sp.sgbc.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sp.sgbc.service.SchedulerService;

@Component
public class ScheduledTasks {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

  @Autowired
  SchedulerService schedulerService;

  @Scheduled(cron = "${schedule.age.raminder:0 0 8 1 1/1 ?}")
  public void notify18YearsAgeRemainder() {
    LOGGER.info("18YearsAgeRemainder process started " + new Date());
    schedulerService.notify18YearsAgeRemainder();
  }

  @Scheduled(cron = "${schedule.transaction.process:0 0 9 1 1/1 ?}")
  public void processTransactionFile() {
    LOGGER.info("Transaction CSV File process started " + new Date());
    schedulerService.processTransactionFile();
  }

  @Scheduled(cron = "${schedule.unpaid.reminder:0 0 10 1 1/1 ?}")
  public void notifyUnpaidApplicants() {
    LOGGER.info("UnpaidApplicants process started " + new Date());
    schedulerService.notifyUnpaidApplicants(3);
    schedulerService.notifyUnpaidApplicants(6);
  }

}
