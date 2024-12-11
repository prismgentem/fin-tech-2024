package org.example.crudkudago.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvertResponse {
    private String fromCurrency;
    private String toCurrency;
    private Double convertedAmount;
}
