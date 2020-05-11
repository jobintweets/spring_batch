package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConditionalJob {
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job job() {
    return this.jobBuilderFactory.get("conditionalJob2")
      .start(firstStep())
      .on("FAILED")
//      .end() // when the step has failed end the job
      .fail()
//    For the failed state, which allows you to rerun the job with the same parameters
//      .on("FAILED").stopAndRestart(successStep())
//      .to(failureStep())
      .from(firstStep()).on("*")
      .to(successStep())
      .end()
      .build();
  }

  @Bean
  public Step firstStep() {
    return this.stepBuilderFactory.get("firstStep")
      .tasklet(passTasklet())
      .build();
  }

  @Bean
  public Tasklet passTasklet() {
    return (contribution, chunkContext) -> {
      return RepeatStatus.FINISHED;
//      throw new RuntimeException("Causing a failure");
    };
  }

  @Bean
  public Step failureStep() {
    return this.stepBuilderFactory.get("failureStep")
      .tasklet(failTasklet())
      .build();
  }

  @Bean
  public Tasklet failTasklet() {
    return (contribution, context) -> {
      System.out.println("Failure!");
      return RepeatStatus.FINISHED;
    };
  }

  @Bean
  public Step successStep() {
    return this.stepBuilderFactory.get("successStep")
      .tasklet(successTasklet())
      .build();
  }

  @Bean
  public Tasklet successTasklet() {
    return (contribution, context) -> {
      System.out.println("Success!");
      return RepeatStatus.FINISHED;
    };
  }

}


