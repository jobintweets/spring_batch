package com.example.springbatch.JdbcBatchItemWriter;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.ItemWriters.FlatfileItemWriterExample;
import com.example.springbatch.ItemWriters.IWCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@SpringBootApplication
@EnableBatchProcessing
public class JdbcBatchWriter {
  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
  @StepScope
  public FlatFileItemReader<IWCustomer> customerFileReader(@Value("#{jobParameters['customerFile']}") Resource inputFile) {
    return new FlatFileItemReaderBuilder<IWCustomer>()
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
      .targetType(IWCustomer.class)
      .build();
  }

  @Bean
  @StepScope
  public JdbcBatchItemWriter<IWCustomer> jdbcCustomerWriter(DataSource dataSource) throws Exception {
    return new JdbcBatchItemWriterBuilder<IWCustomer>()
      .dataSource(dataSource)
      .sql("INSERT INTO IWCUSTOMER (first_name, " + "middle_initial, " +
        "last_name, " +
        "address, " +
        "city, " +
        "state, " +
        "zip) VALUES (?, ?, ?, ?, ?, ?, ?)")
      .itemPreparedStatementSetter(new CustomerItemPreparedStatementSetter())
      .build();
  }

  @Bean
  public Step formatStep() throws Exception {
    return this.stepBuilderFactory.get("itemWriter")
      .<IWCustomer, IWCustomer>chunk(10)
      .reader(customerFileReader(null))
      .writer(jdbcCustomerWriter(null))
      .build();
  }

  @Bean
  public Job formatJob() throws Exception {
    return this.jobBuilderFactory.get("jdbcItemWriter")
      .start(formatStep())
      .incrementer(new RunIdIncrementer())
      .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(JdbcBatchWriter.class, "customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customer.csv");
  }
}
