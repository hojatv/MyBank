package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simplebank.domain.Currency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferDTO {
    private Integer sourceAccountId;
    private Integer destinationAccountId;
    private Float amount;
    private Currency currency;
    private Long etag;
}
