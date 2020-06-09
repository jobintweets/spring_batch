package com.example.springbatch.ItemWriters;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.Processors.ValidationJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@EnableBatchProcessing
@SpringBootApplication
public class FlatfileItemWriterExample {

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
  public FlatFileItemWriter<IWCustomer> customerItemWriter(@Value("#{jobParameters['outputFile']}") Resource outputFile) {
    return new FlatFileItemWriterBuilder<IWCustomer>()
      .name("customerItemWriter")
      .resource(outputFile)
//      .formatted()
//      .format("%s %s lives at %s %s in %s, %s.")
      .delimited()
      .delimiter(";")
      .names(new String[]{"firstName",
        "lastName",
        "address",
        "city",
        "state",
        "zipCode"})
      .build();
  }

  @Bean
  public Step formatStep() {
    return this.stepBuilderFactory.get("formatStep")
      .<IWCustomer, IWCustomer>chunk(10)
      .reader(customerFileReader(null))
      .writer(customerItemWriter(null))
      .build();
  }

  @Bean
	public Job formatJob() {
		return this.jobBuilderFactory.get("formatJobDelimited2")
				.start(formatStep())
      .incrementer(new RunIdIncrementer())
				.build();
	}
  public static void main(String[] args) {
    SpringApplication.run(FlatfileItemWriterExample.class, "customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customer.csv",
     "outputFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/formattedCustomers.txt" );
  }


}
