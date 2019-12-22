package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Balance {
    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private float amount;

    /*a timeStamp based unique tag to control concurrency*/
    private long eTag;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @MapsId
    private Account account;

}
