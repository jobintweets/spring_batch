package com.example.springbatch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.Date;

public class CustomDateIncrementer implements JobParametersIncrementer {
  @Override
  public JobParameters getNext(JobParameters jobParameters) {
    return new JobParametersBuilder(jobParameters) .addDate("currentDate", new Date()) .toJobParameters();
  }
}
