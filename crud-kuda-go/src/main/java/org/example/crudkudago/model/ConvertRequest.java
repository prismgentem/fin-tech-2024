package org.example.crudkudago.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvertRequest {
    private String fromCurrency;
    private String toCurrency;
    private Double amount;
}
