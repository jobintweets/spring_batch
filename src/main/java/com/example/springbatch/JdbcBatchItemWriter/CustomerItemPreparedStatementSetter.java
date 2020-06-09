package com.example.springbatch.JdbcBatchItemWriter;

import com.example.springbatch.ItemReaders.Customer;
import com.example.springbatch.ItemWriters.IWCustomer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/***
 *
 * Prepared sql statement - insert into CUSTOMER (firstName, middleInitial, lastName, address, city,state, zip) values (?, ?, ?, ?, ?, ?, ?)
 To implement the ItemPreparedStatementSetter interface, you create your own CustomerItemPreparedStatementSetter.
 This class implements the single setValues(T item, PreparedStatement ps) method that is required by the
 -ItemPreparedStatementSetter interface by using the normal PreparedStatement API to populate each value of the PreparedStatement with the appropriate value from the item
 */
public class CustomerItemPreparedStatementSetter implements ItemPreparedStatementSetter<IWCustomer> {
  @Override
  public void setValues(IWCustomer iwCustomer, PreparedStatement preparedStatement) throws SQLException {
    preparedStatement.setString(1,iwCustomer.getFirstName());
    preparedStatement.setString(2,iwCustomer.getMiddleInitial());
    preparedStatement.setString(3,iwCustomer.getLastName());
    preparedStatement.setString(4,iwCustomer.getAddress());
    preparedStatement.setString(5,iwCustomer.getCity());
    preparedStatement.setString(6,iwCustomer.getState());
    preparedStatement.setString(7,iwCustomer.getZipCode());
  }
}
