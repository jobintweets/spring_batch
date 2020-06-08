package com.example.springbatch.Processors;

import com.example.springbatch.ItemReaders.Customer;
import org.springframework.batch.item.ItemProcessor;

import javax.swing.*;

//just pass the records with an even zip code to the itemReader.
public class CustomItemProcessor implements ItemProcessor<ProcessorCustomer, ProcessorCustomer> {
  @Override
  public ProcessorCustomer process(ProcessorCustomer processorCustomer) throws Exception {
    return Integer.parseInt(processorCustomer.getZip()) % 2 == 0 ? null: processorCustomer;
  }
}

//  Spring Batch makes this simple by ensuring that any item that results in the ItemProcessor returning null is filtered out.
//  Not only is it filtered out from the downstream impacts (other ItemProcessors or any ItemWriters involved in the step), but Spring Batch keeps a count of the number of records that are filtered and stores it in the job repository.
