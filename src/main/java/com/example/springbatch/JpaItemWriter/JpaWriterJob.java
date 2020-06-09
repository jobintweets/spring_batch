package com.example.springbatch.JpaItemWriter;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.ItemWriters.IWCustomer;
import com.example.springbatch.JdbcBatchItemWriter.JdbcBatchWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
@EnableBatchProcessing
public class JpaWriterJob {

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
  public JpaItemWriter<TestCustomer> jpaItemWriter(EntityManagerFactory entityManagerFactory) {
    JpaItemWriter<TestCustomer> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
  }

  @Bean
  public Step jpaFormatStep() {
    return this.stepBuilderFactory.get("jpaFormatStep")
      .<TestCustomer, TestCustomer>chunk(10)
      .reader(customerFileReader(null))
      .writer(jpaItemWriter(null))
      .build();
  }

  @Bean
  public Job jpaFormatJob() {
    return this.jobBuilderFactory.get("jpaFormatJob")
      .start(jpaFormatStep())
      .incrementer(new RunIdIncrementer())
      .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(JpaWriterJob.class, "customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customer.csv");
  }
}
