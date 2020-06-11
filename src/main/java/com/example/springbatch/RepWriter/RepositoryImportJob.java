package com.example.springbatch.RepWriter;

import com.example.springbatch.JpaItemWriter.TestCustomer;
import com.example.springbatch.web.TestCustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = TestCustomer.class)
@SpringBootApplication
@EnableBatchProcessing
public class RepositoryImportJob {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
  @StepScope
  public FlatFileItemReader<TestCustomer> customerFileReader(@Value("#{jobParameters['customerFile']}") Resource inputFile) {
    return new FlatFileItemReaderBuilder<TestCustomer>()
      .name("customerFileReader")
      .resource(inputFile)
      .delimited()
      .names(new String[]{"firstName",
        "middleInitial",
        "lastName",
        "address",
        "city",
        "state",
        "zipCode"})
      .targetType(TestCustomer.class)
      .build();
  }

  @Bean
  public RepositoryItemWriter<TestCustomer> repositoryItemWriter(TestCustomerRepository repository) {
    return new RepositoryItemWriterBuilder<TestCustomer>()
      .repository(repository)
      .methodName("save")
      .build();
  }

  @Bean
  public Step repositoryFormatStep() throws Exception {
    return this.stepBuilderFactory.get("repositoryFormatStep")
      .<TestCustomer, TestCustomer>chunk(10)
      .reader(customerFileReader(null))
      .writer(repositoryItemWriter(null))
      .build();
  }

  @Bean
  public Job repositoryFormatJob() throws Exception {
    return this.jobBuilderFactory.get("repositoryFormatJob")
      .start(repositoryFormatStep())
      .incrementer(new RunIdIncrementer())
      .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(RepositoryImportJob.class, "customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customer.csv");
  }
}
