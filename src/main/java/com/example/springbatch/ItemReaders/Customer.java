package com.example.springbatch.ItemReaders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  private String firstName;

  private String middleInitial;

  private String lastName;

  private String addressNumber;

  private String street;

  private String city;

  private String state;

  private String zipCode;

  @Override
  public String toString() {
    return "Customer{" +
      "firstName='" + firstName + '\'' +
      ", middleInitial='" + middleInitial + '\'' +
      ", lastName='" + lastName + '\'' +
      ", addressNumber='" + addressNumber + '\'' +
      ", street='" + street + '\'' +
      ", city='" + city + '\'' +
      ", state='" + state + '\'' +
      ", zipCode='" + zipCode + '\'' +
      '}';
  }
}
