package com.example.springbatch.XML;

import com.example.springbatch.Domain.Transaction;
import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.ItemReaders.CustomerTransaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.Collections;
import java.util.List;

@EnableBatchProcessing
@SpringBootApplication
public class XmlReader {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  /***
   * StaxEventItemReader is used to read xml files
   * @param inputFile
   * @return
   */
  @Bean
  @StepScope
  public StaxEventItemReader<Customer> customerFileReader(
    @Value("#{jobParameters['customerFile']}") Resource inputFile) {
    return new StaxEventItemReaderBuilder<Customer>()
      .name("customerFileReader")
      .resource(inputFile)
      .addFragmentRootElements("customer")
      .unmarshaller(customerMarshaller())
      .build();
  }
 //used for mapping into the domain object
  @Bean
	public Jaxb2Marshaller customerMarshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Customer.class, CustomerTransaction.class);
		return jaxb2Marshaller;
	}

  	@Bean
	public ItemWriter itemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

  	@Bean
	public Step copyFileStep() {
		return this.stepBuilderFactory.get("read_from_xml")
				.<Customer, Customer>chunk(10)
				.reader(customerFileReader(null))
				.writer(itemWriter())
				.build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("xml_job6")
				.start(copyFileStep())
				.build();
	}

  	public static void main(String[] args) {
		List<String> realArgs = Collections.singletonList("customerFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/customer.xml");
		SpringApplication.run(XmlReader.class, realArgs.toArray(new String[1]));
	}


}
