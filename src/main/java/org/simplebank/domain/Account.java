package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@Data
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String sourceIBAN;
    private String assignedIBAN;

}
