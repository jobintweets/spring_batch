package com.example.springbatch.Processors;

import com.example.springbatch.ItemReaders.Customer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.HashSet;
import java.util.Set;
//If we wanted the state to be persisted across restarts, the Validator would also need to implement the ItemStream interface by extending ItemStreamSupport and
//  store those last names in the ExecutionContext with each commit.
public class UniqueLastNameValidator extends ItemStreamSupport implements Validator<ProcessorCustomer> {

  private Set<String> lastNames = new HashSet<>();
  @Override
  public void validate(ProcessorCustomer value) throws ValidationException {
    if (lastNames.contains(value.getLastName())) {
      throw new ValidationException("Duplicate last name was found: " + value.getLastName());
    }
    this.lastNames.add(value.getLastName());
  }
//  The update method (called once per chunk once the transaction commits) stores the current state in the ExecutionContext in case there is a failure in the next chunk.
    @Override
    public void update(ExecutionContext executionContext) {
      executionContext.put(getExecutionContextKey("lastNames"), this.lastNames);
    }

//  The open method determines if the lastNames field was saved in a previous execution. If it was, it is restored before the step’s processing begins.
    @Override
    public void open(ExecutionContext executionContext) {
      String lastNames = getExecutionContextKey("lastNames");
      if(executionContext.containsKey(lastNames)) {
        this.lastNames = (Set<String>) executionContext.get(lastNames);
      }
    }
  }
