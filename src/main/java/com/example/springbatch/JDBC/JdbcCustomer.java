package com.example.springbatch.JDBC;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JdbcCustomer {

  private Long id;
  private String firstName;
  private String middleInitial;
  private String lastName;
  private String address;
  private String city;
  private String state;
  private String zipCode;

  @Override
  public String toString() {
    return "JdbcCustomer{" +
      "id=" + id +
      ", firstName='" + firstName + '\'' +
      ", middleInitial='" + middleInitial + '\'' +
      ", lastName='" + lastName + '\'' +
      ", address='" + address + '\'' +
      ", city='" + city + '\'' +
      ", state='" + state + '\'' +
      ", zipCode='" + zipCode + '\'' +
      '}';
  }
}
