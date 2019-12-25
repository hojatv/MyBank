package org.simplebank.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue
    @Column(name = "customer_id")
    private Integer id;

    private String name;

    private String address;

    private String phone;

    private Boolean verified;

}
