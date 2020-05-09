package com.example.springbatch;

import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.batch.core.Job;

import java.util.Arrays;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchApplication {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job job() {
    return this.jobBuilderFactory.get("basic_batch_job_without_param")
      .start(step1())
//      .validator(validatorFunction())
//      .incrementer(new RunIdIncrementer())
      .incrementer(new CustomDateIncrementer())
      .build();
  }


  @Bean
  public Step step1() {
    return this.stepBuilderFactory.get("step3")
      .tasklet(new HelloTasklet())
      .build();
  }



  /**
   *
   * @validator() function will validate the params on the startup
   */
//  @Bean
//  public JobParametersValidator validator()  {
//    DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
//    validator.setRequiredKeys(new String[] {"task","run.id"}); validator.setOptionalKeys(new String[] {"name"});
//    return validator;
//  }

  @Bean
  public CompositeJobParametersValidator validatorFunction()  {
    CompositeJobParametersValidator validator =
      new CompositeJobParametersValidator();
    DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
      new String[] {"task","currentDate"},
      new String[] {"name"});
    defaultJobParametersValidator.afterPropertiesSet();
    validator.setValidators(
      Arrays.asList(new ParameterValidator(),defaultJobParametersValidator));
    return validator;
  }


  /**
   * @value used for late binding .
   * This allows job parameters to be ingested from the command line or other sources and be available for injection when the bean is created.
   */
  @StepScope
  @Bean
  public Tasklet helloWorldTasklet(@Value("#{jobParameters['task']}") String task,
                                   @Value("#{jobParameters['name']}") String name) {
    return (contribution, chunkContext) -> {
      System.out.println(String.format("Task is , %s!", task));
      System.out.println(String.format("Name of framework is   , %s!", name));
      return RepeatStatus.FINISHED;
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringBatchApplication.class, args);
  }

}
