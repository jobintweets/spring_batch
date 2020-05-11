package com.example.springbatch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.core.explore.JobExplorer;

import java.util.List;

public class ExploringTasklet implements Tasklet {

private JobExplorer jobExplorer;

  public ExploringTasklet(JobExplorer jobExplorer1) {
    this.jobExplorer =  jobExplorer1;
  }

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
    String jobName = chunkContext.getStepContext().getJobName();
    List<JobInstance> instances = jobExplorer.getJobInstances(jobName,0,Integer.MAX_VALUE);
    System.out.println(String.format("There are %d job instances for the job %s", instances.size(), jobName));
    System.out.println("They have had the following results"); System.out.println("************************************");

    for (JobInstance instance : instances) {
      List<JobExecution> jobExecutions =
        this.jobExplorer.getJobExecutions(instance);

      System.out.println(
        String.format("Instance %d had %d executions",
          instance.getInstanceId(),
          jobExecutions.size()));

      for (JobExecution jobExecution : jobExecutions) {
        System.out.println(
          String.format("\tExecution %d resulted in Exit Status %s",
            jobExecution.getId(),
            jobExecution.getExitStatus()));
      }
    }

    return null;
  }
}
