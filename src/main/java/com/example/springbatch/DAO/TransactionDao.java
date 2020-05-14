package com.example.springbatch.DAO;

import com.example.springbatch.Domain.Transaction;

import java.util.List;

public interface TransactionDao {
  List<Transaction> getTransactionsByAccountNumber(String accountNumber);
}
