package com.example.springbatch.compositeIW;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.JpaItemWriter.TestCustomer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;

//write all customers who live in a state that starts with the letters A through M to a flat file and items with a state name starting with the letters N through Z to the database.

public class CustomerClassifier implements Classifier<TestCustomer, ItemWriter<? super TestCustomer>> {

  private ItemWriter<TestCustomer> fileItemWriter;
  private ItemWriter<TestCustomer> jdbcItemWriter;

  public CustomerClassifier(StaxEventItemWriter<TestCustomer> fileItemWriter, JdbcBatchItemWriter<TestCustomer> jdbcItemWriter) {
    this.fileItemWriter = fileItemWriter;
    this.jdbcItemWriter = jdbcItemWriter;
  }

  @Override
  public ItemWriter<? super TestCustomer> classify(TestCustomer testCustomer) {
    if(testCustomer.getState().matches("^[A-M].*")) {
      return fileItemWriter;
    } else {
      return jdbcItemWriter;
    }
  }
}
