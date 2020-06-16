package com.example.springbatch.compositeIW;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.ItemWriterAdapters.ItemWriterAdapterJob;
import com.example.springbatch.JpaItemWriter.TestCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableBatchProcessing
public class CompositeItemWriterJob {

  private JobBuilderFactory jobBuilderFactory;

  private StepBuilderFactory stepBuilderFactory;

  public CompositeItemWriterJob(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory) {

    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  @StepScope
  public FlatFileItemReader<TestCustomer> compositewriterItemReader(
    @Value("#{jobParameters['customerFile']}") Resource inputFile) {
    return new FlatFileItemReaderBuilder<TestCustomer>()
      .name("compositewriterItemReader")
      .resource(inputFile)
      .delimited()
      .names(new String[]{"firstName",
        "middleInitial",
        "lastName",
        "address",
        "city",
        "state",
        "zip",
        "email"})
      .targetType(TestCustomer.class)
      .build();
  }

  @Bean
  @StepScope
  public StaxEventItemWriter<TestCustomer> xmlDelegateItemWriter(
    @Value("#{jobParameters['outputFile']}") Resource outputFile) throws Exception {
    Map<String, Class> aliases = new HashMap<>();
    aliases.put("customer", TestCustomer.class);
    XStreamMarshaller marshaller = new XStreamMarshaller();
    marshaller.setAliases(aliases);
    marshaller.afterPropertiesSet();
    return new StaxEventItemWriterBuilder<TestCustomer>()
      .name("customerItemWriter")
      .resource(outputFile)
      .marshaller(marshaller)
      .rootTagName("customers")
      .build();
  }

  @Bean
  public JdbcBatchItemWriter<TestCustomer> jdbcDelgateItemWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<TestCustomer>()
      .namedParametersJdbcTemplate(new NamedParameterJdbcTemplate(dataSource))
      .sql("INSERT INTO TestCustomer (firstName, " +
        "middleInitial, " +
        "lastName, " +
        "address, " +
        "city, " +
        "state, " +
        "zip, " +
        "email) " +
        "VALUES(:firstName, " +
        ":middleInitial, " +
        ":lastName, " +
        ":address, " +
        ":city, " +
        ":state, " +
        ":zip, " +
        ":email)")
      .beanMapped()
      .build();
  }

  @Bean
  public CompositeItemWriter<TestCustomer> compositeItemWriter() throws Exception {
    return new CompositeItemWriterBuilder<TestCustomer>()
      .delegates(Arrays.asList(xmlDelegateItemWriter(null), jdbcDelgateItemWriter(null)))
      .build();
  }

  @Bean
  public Step compositeWriterStep() throws Exception {
    return this.stepBuilderFactory
      .get("compositeWriterStep")
      .<TestCustomer, TestCustomer>chunk(10)
      .reader(compositewriterItemReader(null))
      .writer(compositeItemWriter())
      .build();
  }

  @Bean
  public Job compositeWriterJob() throws Exception {
    return this.jobBuilderFactory
      .get("compositeWriterJob")
      .start(compositeWriterStep())
      .build();
  }
  public static void main(String[] args) {
    SpringApplication.run(CompositeItemWriterJob.class, "customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customerWithEmail.csv",
      "outputFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/result.xml");
  }
}
