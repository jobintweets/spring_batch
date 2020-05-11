package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobExplorer1 {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private JobExplorer jobExplorer;

  @Bean
  public Tasklet explorerTasklet() {
    return new ExploringTasklet(this.jobExplorer);
  }

  @Bean
  public Step explorerStep() {
    return this.stepBuilderFactory.get("explorerStep")
      .tasklet(explorerTasklet())
      .build();
  }
  @Bean
  public Job explorerJob() {
    return this.jobBuilderFactory.get("explorerJob")
      .start(explorerStep())
      .build();
  }


}
