package com.example.springbatch.Services;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.JpaItemWriter.TestCustomer;
import org.springframework.stereotype.Service;

@Service
public class IWAdapterService {
  public void logCustomer(Customer customer) {
    System.out.println("The customer is "  + customer);
  }
}
