package com.example.springbatch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

/***
 *  HelloTasklet class is used for Execution context
 */
public class HelloTasklet implements Tasklet {
  private static final String HELLO_WORLD = "Hello, %s";
  @Value("#{jobParameters['task']}")
  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext context) throws Exception {
    String taskName = (String) context.getStepContext().getJobParameters().get("task");
    ExecutionContext jobContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
//    Holding nothing more than simple key-value pairs,
//    ExecutionContext provides a way to store state within your job in a safe way
    jobContext.put("userName",taskName);
      System.out.println(String.format(HELLO_WORLD, taskName));
    return RepeatStatus.FINISHED;
  }
}
