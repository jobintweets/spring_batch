package com.example.springbatch.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobLauncherBody {

  private String jobName;

  private Properties jobParameters;

  public Properties getJobParamsProperties() {
    return jobParameters;
  }

  public void setJobParamsProperties(Properties jobParameters) {
    this.jobParameters = jobParameters;
  }

  public JobParameters getJobParameters() {
    Properties properties = new Properties();
    properties.putAll(this.jobParameters);
    return new JobParametersBuilder(properties).toJobParameters();
  }

}
