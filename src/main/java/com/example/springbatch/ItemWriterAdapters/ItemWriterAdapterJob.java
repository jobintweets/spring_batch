package com.example.springbatch.ItemWriterAdapters;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.Services.IWAdapterService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@SpringBootApplication
@EnableBatchProcessing
public class ItemWriterAdapterJob {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;


	@Bean
	@StepScope
	public FlatFileItemReader<Customer> customerFileReader(
			@Value("#{jobParameters['customerFile']}") Resource inputFile) {
	  return new FlatFileItemReaderBuilder<Customer>()
				.name("customerFileReader")
				.resource(inputFile)
				.delimited()
				.names(new String[] {"firstName",
						"middleInitial",
						"lastName",
						"address",
						"city",
						"state",
						"zipCode"})
				.targetType(Customer.class)
				.build();
	}

	@Bean
	public ItemWriterAdapter<Customer> itemWriter(customerSerivice cusService) {
		ItemWriterAdapter<Customer> customerItemWriterAdapter = new ItemWriterAdapter<>();
		customerItemWriterAdapter.setTargetObject(cusService);
		customerItemWriterAdapter.setTargetMethod("logCustomer");
		return customerItemWriterAdapter;
	}


	@Bean
	public Step formatStep() throws Exception {
		return this.stepBuilderFactory.get("jpaFormatStep")
				.<Customer, Customer>chunk(10)
				.reader(customerFileReader(null))
				.writer(itemWriter(null))
				.build();
	}

	@Bean
	public Job itemWriterAdapterFormatJob() throws Exception {
		return this.jobBuilderFactory.get("itemWriterAdapterFormatJob")
				.start(formatStep())
				.build();
	}
  public static void main(String[] args) {
    SpringApplication.run(ItemWriterAdapterJob.class, "customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customer.csv");
  }
}
