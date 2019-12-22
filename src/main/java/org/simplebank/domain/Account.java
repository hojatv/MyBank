package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private long id;

    private String sourceIBAN;

    private String assignedIBAN;

    @JoinColumn(name = "customer_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @OneToMany(mappedBy="account", cascade = CascadeType.ALL)
    private List<Balance> balances;

}
