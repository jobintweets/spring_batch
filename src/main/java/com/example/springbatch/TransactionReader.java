package com.example.springbatch;

import com.example.springbatch.Domain.Transaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.transform.FieldSet;

/***
 * We’re reading in a csv, which Spring Batch has robust facilities to do, so why do we need a custom ItemReader?
 * The reason is that the ExitStatus of the step is tied to the state of the reader.
 * If we don’t read in the same number of records as the footer record specifies, we should not continue.
 * So we’re going to wrap a FlatFileItemReader with our custom ItemReader.
 * This custom ItemReader will count the number of records read in. Once it gets to the footer record, if the number of expected records match, processing will continue.
 * However, if they do not, our custom ItemReader will also provide an AfterStep method that will set the ExitStatus to STOPPED.
 */
@Setter
@Getter
public class TransactionReader implements ItemStreamReader<Transaction> {

  private ItemStreamReader<FieldSet> fieldSetReader;

  private int recordCount = 0;

  private int expectedRecordCount = 0;

  public TransactionReader(ItemStreamReader<FieldSet> fieldSetReader) {
    this.fieldSetReader = fieldSetReader;
  }

  public Transaction read() throws Exception {
    return process(fieldSetReader.read());
  }

  /***
   * If there is more than one value in the record, it’s a data record.
   * If there is only one field in the record, it’s the footer record
   * @param fieldSet
   * @return
   */
  private Transaction process(FieldSet fieldSet) {
    Transaction result = null;
    if (fieldSet != null) {

      if (fieldSet.getFieldCount() > 1) {
        result = new Transaction();
        result.setAccountNumber(fieldSet.readString(0));
        result.setTimestamp(fieldSet.readDate(1, "yyyy-MM-DD HH:mm:ss"));
        result.setAmount(fieldSet.readDouble(2));
        recordCount++;
//        footer record
      } else {
        expectedRecordCount = fieldSet.readInt(0);
      }
    }
    return result;
  }

  public void setFieldSetReader(ItemStreamReader<FieldSet> fieldSetReader) {
    this.fieldSetReader = fieldSetReader;
  }
  /***
   *  stop the Job from continuing if the file is invalid. if footer value is 99 and only if 80 entries then the csv file is invalid
   * @param execution
   * @return
   */
  @AfterStep
  public ExitStatus afterStep(StepExecution execution) {
    if (recordCount == expectedRecordCount) {
      return execution.getExitStatus();
    } else {
      return ExitStatus.STOPPED;
    }
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    this.fieldSetReader.open(executionContext);
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    this.fieldSetReader.update(executionContext);
  }

  @Override
  public void close() throws ItemStreamException {
    this.fieldSetReader.close();
  }
}
