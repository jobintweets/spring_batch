package com.example.springbatch.configuration;

import com.example.springbatch.BatchScheduledJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

  // Creating a QuartzJob, QuartzJobDetail
  @Bean
  public JobDetail quartzJobDetail() {
    return JobBuilder.newJob(BatchScheduledJob.class).storeDurably()
      .build();
  }

  // Creating a Trigger to run the QuartzJob which in turn will run the actual job and steps that we have defined.
  @Bean
  public Trigger jobTrigger() {
    SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).withRepeatCount(4);
    return TriggerBuilder.newTrigger()
      .forJob(quartzJobDetail())
      .withSchedule(scheduleBuilder)
      .build();
  }
}

