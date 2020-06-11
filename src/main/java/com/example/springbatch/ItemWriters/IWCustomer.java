package com.example.springbatch.ItemWriters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "IWCUSTOMER")
public class IWCustomer implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private long id;
  @Column
  private String firstName;
  @Column
  private String middleInitial;
  @Column
  private String lastName;
  @Column
  private String address;
  @Column
  private String city;
  @Column
  private String state;
  @Column
  private String zipCode;
  @Column
  private String email;
}
