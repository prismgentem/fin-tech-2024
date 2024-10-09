package org.example.lesson8.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RateResponse {
    private String currency;
    private Double rate;
}
