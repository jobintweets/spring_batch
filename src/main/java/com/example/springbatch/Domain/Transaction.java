package com.example.springbatch.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

  private int id;
  private String accountNumber;

  private Date timestamp;

  private double amount;

}
