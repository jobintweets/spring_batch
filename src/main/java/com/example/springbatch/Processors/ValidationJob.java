package com.example.springbatch.Processors;

import com.example.springbatch.ItemReaders.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@EnableBatchProcessing
@SpringBootApplication
public class ValidationJob {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;
  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
	@StepScope
	public FlatFileItemReader<ProcessorCustomer> customerItemReader(
			@Value("#{jobParameters['customerFile']}")Resource inputFile) {
    return new FlatFileItemReaderBuilder<ProcessorCustomer>()
				.name("customerItemReader")
				.delimited()
				.names(new String[] {
				  "firstName",
						"middleInitial",
						"lastName",
						"address",
						"city",
						"state",
						"zip"})
				.targetType(ProcessorCustomer.class)
				.resource(inputFile)
				.build();
	}
  	@Bean
	public ItemWriter<ProcessorCustomer> itemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

  @Bean
  public UniqueLastNameValidator  validator() {
    UniqueLastNameValidator uniqueLastNameValidator = new UniqueLastNameValidator();
    uniqueLastNameValidator.setName("validator");
    return uniqueLastNameValidator;
  }
//  @Bean
//  public ValidatingItemProcessor<ProcessorCustomer> customerValidatingItemProcessor() {
//    return new ValidatingItemProcessor<>(validator());
//  }

//  @Bean
//  public BeanValidatingItemProcessor<ProcessorCustomer> customerValidatingItemProcessor() {
//    return new BeanValidatingItemProcessor<>();
//  }

//  @Bean
////  ItemProcessorAdapter lets you use existing services as processors for your batch job items.
//  public ItemProcessorAdapter<ProcessorCustomer, ProcessorCustomer> itemProcessor(UpperCaseNameService service)
//  {
//    ItemProcessorAdapter<ProcessorCustomer, ProcessorCustomer> adapter = new ItemProcessorAdapter<>();
//    adapter.setTargetObject(service);
//    adapter.setTargetMethod("upperCase");
//    return adapter;
//  }

  @Bean
  public CustomItemProcessor itemProcessor() {
    return new CustomItemProcessor();
  }


  @Bean
	public Step copyFileStep() {
    return this.stepBuilderFactory.get("execution_context")
				.<ProcessorCustomer, ProcessorCustomer>chunk(5)
      .reader(customerItemReader(null))
//      .processor(customerValidatingItemProcessor())
      .processor(itemProcessor())
      .writer(itemWriter())
      .build();
	}

  @Bean
  public Job job() throws Exception {
    return this.jobBuilderFactory.get("validating_job2") .start(copyFileStep())
      .build();
  }

  public static void main(String[] args) {
		SpringApplication.run(ValidationJob.class, "customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customer.csv");
	}

}
