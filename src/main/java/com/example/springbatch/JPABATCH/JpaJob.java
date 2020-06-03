package com.example.springbatch.JPABATCH;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.JDBC.CustomerRowMapper;
import com.example.springbatch.JDBC.JdbcCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@EnableBatchProcessing
@SpringBootApplication
public class JpaJob {


	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	@StepScope
	public JpaPagingItemReader<JdbcCustomer> customerItemReader(
			EntityManagerFactory entityManagerFactory,
			@Value("#{jobParameters['city']}") String city) {

		return new JpaPagingItemReaderBuilder<JdbcCustomer>()
				.name("customerItemReader")
				.entityManagerFactory(entityManagerFactory)
				.queryString("select c from Customer c where c.city = :city")
				.parameterValues(Collections.singletonMap("city", city))
				.build();
	}

	@Bean
	public ItemWriter<JdbcCustomer> itemWriter() {
		return (items) -> items.forEach(System.out::println);
	}
//
	@Bean
	public Step copyFileStep() {
		return this.stepBuilderFactory.get("jpa_step")
				.<JdbcCustomer, JdbcCustomer>chunk(10)
				.reader(customerItemReader(null, null))
				.writer(itemWriter())
				.build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("jap_job")
				.start(copyFileStep())
				.build();
	}


	public static void main(String[] args) {

		SpringApplication.run(JpaJob.class, "city=Chicago");
	}

}
