package org.example.lesson8.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvertRequest {
    private String fromCurrency;
    private String toCurrency;
    private Double amount;
}
