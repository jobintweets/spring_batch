package com.example.springbatch.JDBC;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// convert the result-set(result of sql query) to the domain object that we have created .
public class CustomerRowMapper  implements RowMapper<JdbcCustomer> {
  @Override
  public JdbcCustomer mapRow(ResultSet resultSet, int i) throws SQLException {
   JdbcCustomer customer =  new JdbcCustomer();
    customer.setId(resultSet.getLong("id"));
    customer.setAddress(resultSet.getString("address"));
    customer.setCity(resultSet.getString("city"));
    customer.setFirstName(resultSet.getString("firstName"));
    customer.setLastName(resultSet.getString("lastName"));
    customer.setMiddleInitial(resultSet.getString("middleInitial"));
    customer.setState(resultSet.getString("state"));
    customer.setZipCode(resultSet.getString("zipCode"));
    return  customer;
  }
}
