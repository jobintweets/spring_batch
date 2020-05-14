package com.example.springbatch;

import com.example.springbatch.DAO.TransactionDao;
import com.example.springbatch.DAO.TransactionDaoSupport;
import com.example.springbatch.Domain.AccountSummary;
import com.example.springbatch.Domain.Transaction;
import com.example.springbatch.Domain.TransactionProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchApplication {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

//  @Bean
//  public Job job() {
//    return this.jobBuilderFactory.get("basic_batch_job_without_param")
//      .start(step1())
////      .validator(validatorFunction())
////      .incrementer(new RunIdIncrementer())
//      .incrementer(new CustomDateIncrementer())
//      .build();
//  }
//
//
//  @Bean
//  public Step step1() {
//    return this.stepBuilderFactory.get("step3")
//      .tasklet(new HelloTasklet())
//      .build();
//  }


  /**
   * @validator() function will validate the params on the startup
   */
//  @Bean
//  public JobParametersValidator validator()  {
//    DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
//    validator.setRequiredKeys(new String[] {"task","run.id"}); validator.setOptionalKeys(new String[] {"name"});
//    return validator;
//  }
  @Bean
  public CompositeJobParametersValidator validatorFunction() {
    CompositeJobParametersValidator validator =
      new CompositeJobParametersValidator();
    DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
      new String[]{"task", "currentDate"},
      new String[]{"name"});
    defaultJobParametersValidator.afterPropertiesSet();
    validator.setValidators(
      Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));
    return validator;
  }


  /**
   * @value used for late binding .
   * This allows job parameters to be ingested from the command line or other sources and be available for injection when the bean is created.
   */
//  @StepScope
//  @Bean
  public Tasklet helloWorldTasklet(@Value("#{jobParameters['task']}") String task,
                                   @Value("#{jobParameters['name']}") String name) {
    return (contribution, chunkContext) -> {
      System.out.println(String.format("Task is , %s!", task));
      System.out.println(String.format("Name of framework is   , %s!", name));
      return RepeatStatus.FINISHED;
    };
  }

////  @Bean
//  public Job restJob() {
//    return this.jobBuilderFactory.get("scheduled_batch_job")
//      .incrementer(new RunIdIncrementer())
//      .start(step1())
//      .build();
//  }
//
////  @Bean
//  public Step step1() {
//    return this.stepBuilderFactory.get("scheduled_step")
//      .tasklet((stepContribution, chunkContext) -> {
//      System.out.println("step1 ran today!");
//      return RepeatStatus.FINISHED;
//      }).build();
//  }

  @Bean
  @StepScope
  public TransactionReader transactionReader() {
    return new TransactionReader(fileItemReader(null));
  }

  @Bean
  @StepScope
  public FlatFileItemReader<FieldSet> fileItemReader(@Value("#{jobParameters['transactionFile']}") Resource inputFile) {
    return new FlatFileItemReaderBuilder<FieldSet>().name("fileItemReader")
      .resource(inputFile)
      .lineTokenizer(new DelimitedLineTokenizer())
      .fieldSetMapper(new PassThroughFieldSetMapper())
      .build();
  }

  @Bean
  public JdbcBatchItemWriter<Transaction> transactionWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Transaction>()
      .itemSqlParameterSourceProvider(
        new BeanPropertyItemSqlParameterSourceProvider<>())
      .sql("INSERT INTO TRANSACTION " +
        "(ACCOUNT_SUMMARY_ID, TIMESTAMP, AMOUNT) " +
        "VALUES ((SELECT ID FROM ACCOUNT_SUMMARY " +
        "	WHERE ACCOUNT_NUMBER = :accountNumber), " +
        ":timestamp, :amount)")
      .dataSource(dataSource)
      .build();
  }

  @Bean
  public Step importTransactionFileStep() {
    return this.stepBuilderFactory.get("importTransactionFileStep")
      .<Transaction, Transaction>chunk(100)
      .reader(transactionReader())
      .writer(transactionWriter(null))
      .allowStartIfComplete(true)
      .listener(transactionReader())
      .build();
  }

  /***
   * <p>
   *    The second Step is to apply the transactions that were found in the file to the accounts
   * </p>
   * <p>
   *    we define a JdbcCursorItemReader to read the AccountSummary records from the database.
   *    The next two bean definitions are for the TransactionDao, which looks up the transactions, and the custom ItemProcessor reviewed in Listing 6-17 that applies the transactions to the accounts.
   *    Finally, the updated account summary records are written with a JdbcBatchItemWriter. With those components configured, we can assemble them into our Step.
   *    The applyTransactionsStep uses the StepBuilderFactory to obtain a builder and configure a chunk-based step with a chunk size of 100 records, and the ItemReader, ItemProcessor, and ItemWriter configured previously.
   * </p>
   *
   */

  @Bean
  @StepScope
  public JdbcCursorItemReader<AccountSummary> accountSummaryReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<AccountSummary>()
      .name("accountSummaryReader")
      .dataSource(dataSource)
      .sql("SELECT ACCOUNT_NUMBER, CURRENT_BALANCE " +
        "FROM ACCOUNT_SUMMARY A " +
        "WHERE A.ID IN (" +
        "	SELECT DISTINCT T.ACCOUNT_SUMMARY_ID " +
        "	FROM TRANSACTION T) " +
        "ORDER BY A.ACCOUNT_NUMBER")
      .rowMapper((resultSet, rowNumber) -> {
        AccountSummary summary = new AccountSummary();
        summary.setAccountNumber(resultSet.getString("account_number"));
        summary.setCurrentBalance(resultSet.getDouble("current_balance"));
        return summary;
      }).build();
  }

  @Bean
  public TransactionDao transactionDao(DataSource dataSource) {
    return new TransactionDaoSupport(dataSource);
  }

  @Bean
  public TransactionProcessor transactionApplierProcessor() {
    return new TransactionProcessor(transactionDao(null));
  }

  @Bean
  public JdbcBatchItemWriter<AccountSummary> accountSummaryWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<AccountSummary>()
      .dataSource(dataSource)
      .itemSqlParameterSourceProvider(
        new BeanPropertyItemSqlParameterSourceProvider<>())
      .sql("UPDATE ACCOUNT_SUMMARY " +
        "SET CURRENT_BALANCE = :currentBalance " +
        "WHERE ACCOUNT_NUMBER = :accountNumber")
      .build();
  }

  /***
   *  <p>
   *     starts off configuring the ItemWriter. The FlatFileItemWriter generates a CSV
   * with the account number and current balance in each record.
   * The Step is then assembled using the StepBuilderFactory to obtain a builder and configure a chunk-based Step
   * with the reader used in the previous Step (accountSummaryReader) and the ItemWriter we just configured.
   *  </p>
   * @return
   */
  @Bean
  public Step applyTransactionsStep() {
    return this.stepBuilderFactory.get("applyTransactionsStep")
      .<AccountSummary, AccountSummary>chunk(100)
      .reader(accountSummaryReader(null))
      .processor(transactionApplierProcessor())
      .writer(accountSummaryWriter(null))
      .build();
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<AccountSummary> accountSummaryFileWriter(
    @Value("#{jobParameters['summaryFile']}") Resource summaryFile) {

    DelimitedLineAggregator<AccountSummary> lineAggregator =
      new DelimitedLineAggregator<>();
    BeanWrapperFieldExtractor<AccountSummary> fieldExtractor =
      new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(new String[] {"accountNumber", "currentBalance"});
    fieldExtractor.afterPropertiesSet();
    lineAggregator.setFieldExtractor(fieldExtractor);

    return new FlatFileItemWriterBuilder<AccountSummary>()
      .name("accountSummaryFileWriter")
      .resource(summaryFile)
      .lineAggregator(lineAggregator)
      .build();
  }

  @Bean
  public Step generateAccountSummaryStep() {
    return this.stepBuilderFactory.get("generateAccountSummaryStep")
      .<AccountSummary, AccountSummary>chunk(100)
      .reader(accountSummaryReader(null))
      .writer(accountSummaryFileWriter(null))
      .build();
  }

  @Bean
  public Job transactionJob() {
    return this.jobBuilderFactory.get("newTransactionJob7")
      .start(importTransactionFileStep())
      .on("STOPPED")
      .stopAndRestart(importTransactionFileStep())
      .from(importTransactionFileStep())
      .on("*")
      .to(applyTransactionsStep())
      .from(applyTransactionsStep())
      .next(generateAccountSummaryStep()).end()
      .build();
  }

  public static void main(String[] args) {
    List<String> realArgs = new ArrayList<>(Arrays.asList(args));

    realArgs.add("transactionFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/input/transactionFile.csv");
    realArgs.add("summaryFile=file:/Users/jobingeorge/Desktop/spring-batch/src/main/resources/summaryFile.csv");
    SpringApplication.run(SpringBatchApplication.class, realArgs.toArray(new String[realArgs.size()]));

  }

}
