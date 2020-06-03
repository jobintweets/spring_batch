package com.example.springbatch.JdbcPaginationReader;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.JDBC.CursorApproach;
import com.example.springbatch.JDBC.CustomerRowMapper;
import com.example.springbatch.JDBC.JdbcCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableBatchProcessing
@SpringBootApplication
public class JdbcPagination {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  @StepScope
  public JdbcPagingItemReader<JdbcCustomer> customerItemReader(DataSource dataSource,
                                                               PagingQueryProvider queryProvider,
                                                               @Value("#{jobParameters['city']}") String city) {

    Map<String, Object> parameterValues = new HashMap<>(1);
    parameterValues.put("city", city);

    return new JdbcPagingItemReaderBuilder<JdbcCustomer>()
      .name("customerItemReader")
      .dataSource(dataSource)
      .queryProvider(queryProvider)
      .parameterValues(parameterValues)
      .pageSize(10)
      .rowMapper(new CustomerRowMapper())
      .build();
  }

  /***
   * Since using the SqlPagingQueryProviderFactoryBean will usually provide us with what we want by autodetecting what database platform you are using and returning the appropriate PagingQueryProvider,
   * we will use it for our examples.
   * Used for the pagination data
   * @param dataSource
   * @return
   */
  @Bean
  public SqlPagingQueryProviderFactoryBean pagingQueryProvider(DataSource dataSource) {
    SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
    factoryBean.setDataSource(dataSource);
    factoryBean.setSelectClause("select *");
    factoryBean.setFromClause("from Customer");
    factoryBean.setWhereClause("where city = :city");
    factoryBean.setSortKey("lastName");
    return factoryBean;
  }
  @Bean
  public ItemWriter itemWriter() {
    return (items) -> items.forEach(System.out::println);
  }

  @Bean
  public Step copyFileStep() {
    return this.stepBuilderFactory.get("paginated_step")
      .<JdbcCustomer, JdbcCustomer>chunk(10)
      .reader(customerItemReader(null,null,null))
      .writer(itemWriter())
      .build();
  }

  @Bean
  public Job job() {
    return this.jobBuilderFactory.get("paginated_job")
      .start(copyFileStep())
      .build();
  }

  public static void main(String[] args) {
    SpringApplication.run( JdbcPagination.class,"city=Chicago");

  }
}
