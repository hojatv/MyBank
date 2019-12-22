package org.simplebank.domain;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
public class Customer {
    public Customer() {
    }
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String address;
    private String phone;
    private Boolean verified;



}
