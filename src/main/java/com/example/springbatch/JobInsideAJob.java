package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowStep;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class JobInsideAJob {
  /***
   * calling a job from the step of another job.
   * conditionalStepLogicJob() job's first step will call the preProcessingJob() job, which in turn has 3 steps .
   */
  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

//  @Bean
  public Tasklet loadStockFile() {
    return (contribution, chunkContext) -> {
      System.out.println("The stock file has been loaded");
      return RepeatStatus.FINISHED;
    };
  }

//  @Bean
  public Tasklet loadCustomerFile() {
    return (contribution, chunkContext) -> {
      System.out.println("The customer file has been loaded");
      return RepeatStatus.FINISHED;
    };
  }

//  @Bean
  public Tasklet updateStart() {
    return (contribution, chunkContext) -> {
      System.out.println("The start has been updated");
      return RepeatStatus.FINISHED;
    };
  }

//  @Bean
  public Tasklet runBatchTasklet() {
    return (contribution, chunkContext) -> {
      System.out.println("The batch has been run");
      return RepeatStatus.FINISHED;
    };
  }

//  @Bean
  public Job preProcessingJob() {
    return this.jobBuilderFactory.get("preProcessingJob")
      .start(loadFileStep())
      .next(loadCustomerStep())
      .next(updateStartStep())
      .build();
  }

//  @Bean
  public Job conditionalStepLogicJob() {
    return this.jobBuilderFactory.get("conditionalStepLogicJob")
      .start(intializeBatch())
      .next(runBatch())
      .build();
  }

//  @Bean
  public Step intializeBatch() {
    return this.stepBuilderFactory.get("initalizeBatch")
      //calling a job form the step
      .job(preProcessingJob())
      //define a class to extract
      //the parameters from either the JobParameters of the parent job or the ExecutionContext
      // (the DefaultJobParameterExtractor checks both places) and pass those parameters to the child job preProcessingJob()
      .parametersExtractor(new DefaultJobParametersExtractor())
      .build();
  }

  /***
   *
   * @Bean this bean is used for the FlowStep
   */
//  @Bean
//  public Step intializeBatch() {
//    return this.stepBuilderFactory.get("initalizeBatch")
//      .flow(preProcessingFlow())
//      .build();
//  }

//  @Bean
  public Step loadFileStep() {
    return this.stepBuilderFactory.get("loadFileStep").tasklet(loadStockFile())
      .build();
  }

//  @Bean
  public Step loadCustomerStep() {
    return this.stepBuilderFactory.get("loadCustomerStep").tasklet(loadCustomerFile())
      .build();
  }

//  @Bean
  public Step updateStartStep() {
    return this.stepBuilderFactory.get("updateStartStep").tasklet(updateStart())
      .build();
  }

//  @Bean
  public Step runBatch() {
    return this.stepBuilderFactory.get("runBatch").tasklet(runBatchTasklet())
      .build();
  }

//  Using a FlowStep allows you to see the impact of the flow as a whole
//  instead of having to aggregate the individual steps.
//  @Bean
  public Flow preProcessingFlow() {
    return new FlowBuilder<Flow>("preProcessingFlow")
      .start(loadFileStep())
      .next(loadCustomerStep())
      .next(updateStartStep()).build();
  }

}
