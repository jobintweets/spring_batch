package com.example.springbatch.Domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AccountSummary  {

  private int id;

  private String accountNumber;

  private Double currentBalance;
}
