package com.example.springbatch.RepositoryReader;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.JDBC.JdbcCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;

import java.util.Collections;

@EnableBatchProcessing
@SpringBootApplication
public class RepositoryJob {


	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public RepositoryItemReader<JdbcCustomer> customerItemReader(CustomerRepository repository,
                                                           @Value("#{jobParameters['city']}") String city) {

		return new RepositoryItemReaderBuilder<JdbcCustomer>()
				.name("customerItemReader")
				.arguments(Collections.singletonList(city))
				.methodName("findByCity")
				.repository(repository)
				.sorts(Collections.singletonMap("lastName", Sort.Direction.ASC))
				.build();
	}

	@Bean
	public ItemWriter<JdbcCustomer> itemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

	@Bean
	public Step copyFileStep() {
		return this.stepBuilderFactory.get("repository")
				.<JdbcCustomer,JdbcCustomer>chunk(10)
				.reader(customerItemReader(null, null))
				.writer(itemWriter())
				.build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("repositoryjob")
				.start(copyFileStep())
				.build();
	}


	public static void main(String[] args) {

		SpringApplication.run(RepositoryJob.class, "city=Chicago");
	}

}
