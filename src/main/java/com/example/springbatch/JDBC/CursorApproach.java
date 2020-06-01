package com.example.springbatch.JDBC;

import com.example.springbatch.ItemReaders.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@EnableBatchProcessing
@SpringBootApplication
public class CursorApproach {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  // we are reading from the customer table
  @Bean
  public JdbcCursorItemReader<JdbcCustomer> customerItemReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<JdbcCustomer>()
      .name("customerItemReader")
      .dataSource(dataSource)
      .sql("select * from customer")
      .rowMapper(new CustomerRowMapper())
      .build();
  }
  @Bean
  public ItemWriter itemWriter() {
    return (items) -> items.forEach(System.out::println);
  }

  @Bean
  public Step copyFileStep() {
    return this.stepBuilderFactory.get("cursor_step")
      .<JdbcCustomer, JdbcCustomer>chunk(10)
      .reader(customerItemReader(null))
      .writer(itemWriter())
      .build();
  }

  @Bean
  public Job job() {
    return this.jobBuilderFactory.get("cursor_job")
      .start(copyFileStep())
      .build();
  }

  public static void main(String[] args) {
SpringApplication.run(CursorApproach.class,args);
  }

}
