package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Integer id;

    private String sourceIBAN;

    private String assignedIBAN;

    @JoinColumn(name = "customer_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

}
