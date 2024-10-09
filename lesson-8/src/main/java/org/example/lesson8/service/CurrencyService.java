package org.example.lesson8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.lesson8.client.CbrClient;
import org.example.lesson8.controller.request.ConvertRequest;
import org.example.lesson8.controller.response.ConvertResponse;
import org.example.lesson8.controller.response.RateResponse;
import org.example.lesson8.exception.ErrorType;
import org.example.lesson8.exception.ServiceException;
import org.example.lesson8.util.XmlCurrencyParser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CbrClient cbrClient;

    @Cacheable(value = "currencyRates", key = "#currencyCode")
    public RateResponse getRate(String currencyCode) {
        log.info("Fetching rate for currency: {}", currencyCode);

        validateCurrencyCode(currencyCode);

        var xml = Optional.ofNullable(cbrClient.getCurrencyRates().block())
                .orElseThrow(() -> {
                    log.error("Failed to fetch currency rates from CBR");
                    return new ServiceException(ErrorType.SERVICE_UNAVAILABLE, "Could not retrieve data from CBR");
                });

        var rate = parseCurrencyRate(xml, currencyCode);

        log.info("Successfully fetched rate for currency: {}. Rate: {}", currencyCode, rate);
        return RateResponse.builder()
                .currency(currencyCode)
                .rate(rate)
                .build();
    }

    public ConvertResponse convert(ConvertRequest request) {
        log.info("Converting from {} to {} with amount {}", request.getFromCurrency(), request.getToCurrency(), request.getAmount());

        validateConvertRequest(request);

        var xml = Optional.ofNullable(cbrClient.getCurrencyRates().block())
                .orElseThrow(() -> {
                    log.error("Failed to fetch currency rates from CBR for conversion");
                    return new ServiceException(ErrorType.SERVICE_UNAVAILABLE, "Could not retrieve data from CBR");
                });

        var fromRate = parseCurrencyRate(xml, request.getFromCurrency());
        var toRate = parseCurrencyRate(xml, request.getToCurrency());

        double convertedAmount = calculateConvertedAmount(request.getAmount(), fromRate, toRate);

        log.info("Successfully converted {} {} to {} {} with result: {}", request.getAmount(), request.getFromCurrency(), convertedAmount, request.getToCurrency(), convertedAmount);

        return ConvertResponse.builder()
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .convertedAmount(convertedAmount)
                .build();
    }

    private void validateConvertRequest(ConvertRequest request) {
        if (request == null || request.getAmount() <= 0) {
            log.error("Invalid conversion request: amount must be greater than zero");
            throw new ServiceException(ErrorType.INVALID_REQUEST, "Amount must be greater than zero");
        }

        if (request.getFromCurrency() == null || request.getToCurrency() == null) {
            log.error("Invalid conversion request: both fromCurrency and toCurrency must be provided");
            throw new ServiceException(ErrorType.INVALID_REQUEST, "Both fromCurrency and toCurrency must be provided");
        }
    }

    private void validateCurrencyCode(String currencyCode) {
        if (currencyCode == null || currencyCode.isBlank()) {
            log.error("Invalid currency code: must be provided");
            throw new ServiceException(ErrorType.INVALID_REQUEST, "Currency code must be provided");
        }
    }

    private Double parseCurrencyRate(String xml, String currencyCode) {
        try {
            log.debug("Parsing rate for currency: {}", currencyCode);
            return XmlCurrencyParser.getCurrencyRate(xml, currencyCode);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("Error parsing XML response for currency: {}", currencyCode, e);
            throw new ServiceException(ErrorType.INVALID_REQUEST, "Error parsing XML response");
        } catch (IllegalArgumentException e) {
            log.error("Currency not found: {}", currencyCode, e);
            throw new ServiceException(ErrorType.CURRENCY_NOT_FOUND, "Currency not found: %s".formatted(currencyCode));
        }
    }

    private double calculateConvertedAmount(double amount, double fromRate, double toRate) {
        log.debug("Calculating conversion: amount={}, fromRate={}, toRate={}", amount, fromRate, toRate);
        return amount * toRate / fromRate;
    }
}
