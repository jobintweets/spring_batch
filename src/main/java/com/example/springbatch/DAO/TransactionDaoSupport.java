package com.example.springbatch.DAO;

import com.example.springbatch.Domain.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class TransactionDaoSupport  extends JdbcTemplate implements TransactionDao {

  public TransactionDaoSupport(DataSource dataSource) {
    super(dataSource);
  }

  /***
   * selects all Transaction records associated with the accountNumber provided and returns them.
   * We’ll use it in an ItemProcessor that will apply all the transactions to a given account to determine their current balance
   * @param accountNumber
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
    return query(
      "select t.id, t.timestamp, t.amount " +
        "from transaction t inner join account_summary a on " +
        "a.id = t.account_summary_id " +
        "where a.account_number = ?",
      new Object[] { accountNumber }, (rs, rowNum) -> {
        Transaction trans = new Transaction();
        trans.setAmount(rs.getDouble("amount"));
        trans.setTimestamp(rs.getDate("timestamp"));
        return trans;
      }
    );
  }
}
