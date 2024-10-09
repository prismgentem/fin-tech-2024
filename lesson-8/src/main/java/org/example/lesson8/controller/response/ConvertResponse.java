package org.example.lesson8.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvertResponse {
    private String fromCurrency;
    private String toCurrency;
    private Double convertedAmount;
}
