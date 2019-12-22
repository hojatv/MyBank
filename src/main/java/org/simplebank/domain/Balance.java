package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@Data
public class Balance {
    @Id
    @GeneratedValue
    private Long id;
    private Long accountId;
    private Currency currency;
    private Float amount;

    /*a timeStamp based unique tag to control concurrency*/
    private String eTag;

}
