package com.example.springbatch.Domain;

import com.example.springbatch.DAO.TransactionDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionProcessor implements ItemProcessor<AccountSummary,AccountSummary> {

  private TransactionDao transactionDao;

  @Override
  public AccountSummary process(AccountSummary summary) throws Exception {
    List<Transaction> transactions = transactionDao.getTransactionsByAccountNumber(summary.getAccountNumber());
    for (Transaction transaction : transactions) {
      summary.setCurrentBalance(summary.getCurrentBalance() + transaction.getAmount());
    }
    return summary;
  }
}
