package com.example.springbatch.Processors;

import com.example.springbatch.ItemReaders.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProcessorCustomer {

  @NotNull(message="First name is required")
  @Pattern(regexp="[a-zA-Z]+", message="First name must be alphabetical")
  private String firstName;

  @Size(min=1, max=1)
  @Pattern(regexp="[a-zA-Z]", message="Middle initial must be alphabetical")
  private String middleInitial;

  @NotNull(message="Last name is required")
  @Pattern(regexp="[a-zA-Z]+", message="Last name must be alphabetical")
  private String lastName;

  @NotNull(message="Address is required")
  @Pattern(regexp="[0-9a-zA-Z\\. ]+")
  private String address;

  @NotNull(message="City is required")
  @Pattern(regexp="[a-zA-Z\\. ]+")
  private String city;

  @NotNull(message="State is required")
  @Size(min=2,max=2)
  @Pattern(regexp="[A-Z]{2}")
  private String state;

  @NotNull(message="Zip is required")
  @Size(min=5,max=5)
  @Pattern(regexp="\\d{5}")
  private String zip;

  public ProcessorCustomer(ProcessorCustomer original) {
    this.firstName = original.getFirstName();
    this.middleInitial = original.getMiddleInitial();
    this.lastName = original.getLastName();
    this.address = original.getAddress();
    this.city = original.getCity();
    this.state = original.getState();
    this.zip = original.getZip();
  }
  @Override
  public String toString() {
    return "ProcessorCustomer{" +
      "firstName='" + firstName + '\'' +
      ", middleInitial='" + middleInitial + '\'' +
      ", lastName='" + lastName + '\'' +
      ", address='" + address + '\'' +
      ", city='" + city + '\'' +
      ", state='" + state + '\'' +
      ", zip='" + zip + '\'' +
      '}';
  }
}
