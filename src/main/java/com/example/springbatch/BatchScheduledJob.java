package com.example.springbatch;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class BatchScheduledJob extends QuartzJobBean {

  @Autowired
  private Job job;

  @Autowired
  private JobExplorer jobExplorer;

  @Autowired
  private JobLauncher jobLauncher;

  // Launching the Job
  // executeInternal method will be called once each time the scheduled event fires
  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
      .getNextJobParameters(this.job).
        toJobParameters();
    try {
      //Getting the job bean and executing the steps and job.
      this.jobLauncher.run(this.job, jobParameters);
    }
    catch (Exception e) {
      e.printStackTrace(); }
  }
}
