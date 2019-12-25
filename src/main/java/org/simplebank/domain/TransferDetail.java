package org.simplebank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferDetail {
    private String errorMessage;
    private Status status;
}
