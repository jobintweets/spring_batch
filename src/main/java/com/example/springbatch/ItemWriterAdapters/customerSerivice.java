package com.example.springbatch.ItemWriterAdapters;

import com.example.springbatch.ItemReaders.Customer;
import org.springframework.stereotype.Service;

@Service
public class customerSerivice {
  public void logCustomer(Customer customer) {
    System.out.println("The customer is "  + customer);
  }
}
