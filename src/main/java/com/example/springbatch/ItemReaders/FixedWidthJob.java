package com.example.springbatch.ItemReaders;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;


import java.util.Arrays;
import java.util.List;

@EnableBatchProcessing
@SpringBootApplication
public class FixedWidthJob {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Step copyFileStep() {
    return this.stepBuilderFactory.get("delimited_file")
      .<Customer, Customer>chunk(10)
      .reader(customerItemReader(null))
      .writer(itemWriter())
      .build();
  }

  @Bean
  public Job job() {
    return this.jobBuilderFactory.get("delimited_job")
      .start(copyFileStep())
      .build();
  }


//  @Bean
//  @StepScope
//  public FlatFileItemReader<Customer> customerItemReader(
//    @Value("#{jobParameters['customerFile']}") Resource inputFile) {
//    return new FlatFileItemReaderBuilder<Customer>()
//      .name("customerItemReader")
//      .resource(inputFile)
//      .fixedLength()
//      .columns(new Range[]{new Range(1, 11),
//        new Range(12, 12), new Range(13, 22),
//        new Range(23, 26), new Range(27, 46),
//        new Range(47, 62), new Range(63, 64),
//        new Range(65, 69)})
//      .names(new String[]{"firstName",
//        "middleInitial", "lastName",
//        "addressNumber", "street",
//        "city", "state",
//        "zipCode"})
//      .targetType(Customer.class)
//      .build();
//  }
@Bean
@StepScope
public FlatFileItemReader<Customer> customerItemReader(
  @Value("#{jobParameters['customerFile']}") Resource inputFile) {
  return new FlatFileItemReaderBuilder<Customer>()
    .name("customerItemReader")
    .resource(inputFile)
    .delimited()
    .names(new String[]{"firstName",
      "middleInitial",
      "lastName",
      "addressNumber",
      "street",
      "city",
      "state",
      "zipCode"})
    .targetType(Customer.class)
    .build();
}

  @Bean
  public ItemWriter<Customer> itemWriter() {
    return (items) -> items.forEach(System.out::println);
  }


  public static void main(String[] args) {
//    List<String> realArgs = Arrays.asList("customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customerFixedWidth.txt");
    List<String> realArgs = Arrays.asList("customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customers.txt");

    SpringApplication.run(FixedWidthJob.class, realArgs.toArray(new String[1]));
  }

}
