package com.example.springbatch.MultiResourceIW;

import com.example.springbatch.ItemWriters.IWCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableBatchProcessing
@Configuration
public class MultiResourceJob {

  private JobBuilderFactory jobBuilderFactory;

  private StepBuilderFactory stepBuilderFactory;

  public MultiResourceJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  @Bean
  public JdbcCursorItemReader<IWCustomer> customerJdbcCursorItemReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<IWCustomer>()
      .name("customerItemReader")
      .dataSource(dataSource)
      .sql("select * from iwcustomer")
      .rowMapper(new BeanPropertyRowMapper<>(IWCustomer.class))
      .build();
  }

  @Bean
	@StepScope
	public StaxEventItemWriter<IWCustomer> delegateItemWriter() throws Exception {

		Map<String, Class> aliases = new HashMap<>();
		aliases.put("customer", IWCustomer.class);

		XStreamMarshaller marshaller = new XStreamMarshaller();

		marshaller.setAliases(aliases);

		marshaller.afterPropertiesSet();

		return new StaxEventItemWriterBuilder<IWCustomer>()
				.name("customerItemWriter")
				.marshaller(marshaller)
				.rootTagName("customers")
				.build();
	}

  	@Bean
	public MultiResourceItemWriter<IWCustomer> multiCustomerFileWriter() throws Exception {
    return new MultiResourceItemWriterBuilder<IWCustomer>()
				.name("multiCustomerFileWriter")
				.delegate(delegateItemWriter())
				.itemCountLimitPerResource(5)
				.resource(new FileSystemResource("target/customer.xml"))
				.build();
	}

	@Bean
	public Step multiXmlGeneratorStep() throws Exception {
		return this.stepBuilderFactory.get("multiXmlGeneratorStep1")
				.<IWCustomer, IWCustomer>chunk(10)
				.reader(customerJdbcCursorItemReader(null))
				.writer(multiCustomerFileWriter())
				.build();
	}

	@Bean
	public Job xmlGeneratorJob() throws Exception {
		return this.jobBuilderFactory.get("xmlMultipleGeneratorJob")
				.start(multiXmlGeneratorStep())
      .incrementer(new RunIdIncrementer())
				.build();
	}
  public static void main(String[] args) {
    SpringApplication.run(MultiResourceJob.class);
  }
}

