package com.example.springbatch.ItemWriterAdapters;

import com.example.springbatch.ItemReaders.Customer;
import org.springframework.stereotype.Service;

@Service
public class customerSerivice {
  public void logCustomer(Customer customer) {
    System.out.println("The customer is " + customer);
  }

  public void logCustomerAddress(String address,
                                 String city,
                                 String state,
                                 String zip) {
    System.out.println(
      String.format("I just saved the address:\n%s\n%s, %s\n%s",
        address,
        city,
        state,
        zip));
  }
}
