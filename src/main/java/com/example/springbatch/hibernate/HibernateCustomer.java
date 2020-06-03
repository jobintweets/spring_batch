package com.example.springbatch.hibernate;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hibernate_customer")
@NoArgsConstructor
@AllArgsConstructor
public class HibernateCustomer {

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
    return "HibernateCustomer{" +
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
