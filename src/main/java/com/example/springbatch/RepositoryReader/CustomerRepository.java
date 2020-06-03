package com.example.springbatch.RepositoryReader;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.JDBC.JdbcCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository  extends JpaRepository<JdbcCustomer, Long> {
  Page<JdbcCustomer> findByCity(String city, Pageable pageRequest);
}
