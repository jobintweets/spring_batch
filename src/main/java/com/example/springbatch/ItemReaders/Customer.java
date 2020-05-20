package com.example.springbatch.ItemReaders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// for xml mapping
@XmlRootElement
public class Customer {

  private String firstName;

  private String middleInitial;

  private String lastName;

  private String address;

  private String city;

  private String state;

  private String zipCode;

  private List<CustomerTransaction> transactions;

  @XmlElementWrapper(name = "transactions")
  @XmlElement(name = "transaction")
  public void setTransactions(List<CustomerTransaction> transactions) {
    this.transactions = transactions;
  }

  @Override
  public String toString() {
    return "Customer{" +
      "firstName='" + firstName + '\'' +
      ", middleInitial='" + middleInitial + '\'' +
      ", lastName='" + lastName + '\'' +
      ", address='" + address + '\'' +
      ", city='" + city + '\'' +
      ", state='" + state + '\'' +
      ", zipCode='" + zipCode + '\'' +
      ", transactions=" + transactions +
      '}';
  }
}
