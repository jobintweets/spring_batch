package com.example.springbatch.JDBC;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class JdbcCustomer {

  @Id
  private Long id;
  @Column(name = "firstName")
  private String firstName;
  @Column(name = "middleInitial")
  private String middleInitial;
  @Column(name = "lastName")
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
