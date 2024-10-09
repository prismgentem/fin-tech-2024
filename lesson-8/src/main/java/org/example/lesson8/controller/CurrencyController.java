package org.example.lesson8.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.example.lesson8.controller.request.ConvertRequest;
import org.example.lesson8.controller.response.ConvertResponse;
import org.example.lesson8.controller.response.RateResponse;
import org.example.lesson8.service.CurrencyService;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @Operation(summary = "Получить курс валюты по коду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс валюты найден"),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена")
    })
    @GetMapping("/rates/{code}")
    public ResponseEntity<RateResponse> getRate(@PathVariable String code) {
        RateResponse rateResponse = currencyService.getRate(code.toUpperCase());
        return ResponseEntity.ok(rateResponse);
    }

    @Operation(summary = "Конвертировать валюту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Конвертация успешна"),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос")
    })
    @PostMapping("/convert")
    public ResponseEntity<ConvertResponse> convertCurrency(@RequestBody ConvertRequest convertRequest) {
        ConvertResponse convertResponse = currencyService.convert(convertRequest);
        return ResponseEntity.ok(convertResponse);
    }
}
