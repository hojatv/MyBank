package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"currency", "account_id"})
)
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "balance_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Float amount;

    /*a timeStamp based unique tag making transactions more secure*/
    private Long eTag;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}

