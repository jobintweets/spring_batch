package com.example.springbatch.ItemReaders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlType(name = "transaction")
public class CustomerTransaction {

  private String accountNumber;
  private Date transactionDate;
  private Double amount;

  private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

  public Date getTransactionDate() {
    return transactionDate;
  }

  @Override
  public String toString() {
    return "CustomerTransaction{" +
      "accountNumber='" + accountNumber + '\'' +
      ", transactionDate=" + transactionDate +
      ", amount=" + amount +
      '}';
  }
}
