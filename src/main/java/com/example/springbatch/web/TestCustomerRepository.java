package com.example.springbatch.web;

import com.example.springbatch.JpaItemWriter.TestCustomer;
import org.springframework.data.repository.CrudRepository;


public interface TestCustomerRepository extends CrudRepository<TestCustomer, Long> {
}
