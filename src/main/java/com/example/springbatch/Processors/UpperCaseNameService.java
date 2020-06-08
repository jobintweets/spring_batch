package com.example.springbatch.Processors;

import org.springframework.stereotype.Service;

@Service
//. It will have a single method that copies the Customer input object into a new Customer output object (making it idempotent),
// and then uppercases the name values on the new instance, returning that once it’s done.
public class UpperCaseNameService {
  public ProcessorCustomer upperCase(ProcessorCustomer customer) {
    ProcessorCustomer newCustomer = new ProcessorCustomer(customer);
    newCustomer.setFirstName(newCustomer.getFirstName().toUpperCase());
    newCustomer.setMiddleInitial(newCustomer.getMiddleInitial().toUpperCase());
    newCustomer.setLastName(newCustomer.getLastName().toUpperCase());
    return newCustomer;
  }
}
