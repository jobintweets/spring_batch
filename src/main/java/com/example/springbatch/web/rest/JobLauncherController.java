package com.example.springbatch.web.rest;

import com.example.springbatch.Domain.JobLauncherBody;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class JobLauncherController {

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private ApplicationContext context;

  @Autowired
  private JobExplorer jobExplorer;


  @PostMapping(path = "/run")
  public ExitStatus runJob(@RequestBody JobLauncherBody request) throws Exception {
    Job job = this.context.getBean(request.getJobName(), Job.class);
    JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(), this.jobExplorer)
      .getNextJobParameters(job).toJobParameters();
    return this.jobLauncher.run(job, jobParameters).getExitStatus();
  }
}
