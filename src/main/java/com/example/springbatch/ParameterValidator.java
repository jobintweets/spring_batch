package com.example.springbatch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class ParameterValidator implements JobParametersValidator {
  @Override
  public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
    String taskName = jobParameters.getString("task");
    if (taskName == null) {
      throw new JobParametersInvalidException("The task name cannot be null");
    }
  }
}
