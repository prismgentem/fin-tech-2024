package org.example.crudkudago.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.client.CbrClient;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.model.ConvertRequest;
import org.example.crudkudago.model.ConvertResponse;
import org.example.crudkudago.util.XmlCurrencyParser;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {
    private final CbrClient cbrClient;

    public ConvertResponse convert(ConvertRequest request) {
        log.info("Converting {} from {} to {}", request.getAmount(), request.getFromCurrency(), request.getToCurrency());

        validateConvertRequest(request);

        var xml = Optional.ofNullable(cbrClient.getCurrencyRates().block())
                .orElseThrow(() -> {
                    log.error("Failed to fetch currency rates from CBR for conversion");
                    return new ServiceException(ErrorType.SERVICE_UNAVAILABLE, "Could not retrieve data from CBR");
                });

        double fromRate = "RUB".equalsIgnoreCase(request.getFromCurrency().trim())
                ? 1.0
                : parseCurrencyRate(xml, request.getFromCurrency().trim());

        double convertedAmount = request.getAmount() * fromRate;

        log.info("Successfully converted {} {} to {} with result: {}", request.getAmount(), request.getFromCurrency(), request.getToCurrency(), convertedAmount);

        return ConvertResponse.builder()
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .convertedAmount(convertedAmount)
                .build();
    }

    private void validateConvertRequest(ConvertRequest request) {
        if (request == null || request.getAmount() <= 0) {
            log.error("Invalid conversion request: amount must be greater than zero");
            throw new ServiceException(ErrorType.BAD_REQUEST, "Amount must be greater than zero");
        }

        if (request.getFromCurrency() == null || request.getToCurrency() == null) {
            log.error("Invalid conversion request: both fromCurrency and toCurrency must be provided");
            throw new ServiceException(ErrorType.BAD_REQUEST, "Both fromCurrency and toCurrency must be provided");
        }
    }

    private Double parseCurrencyRate(String xml, String currencyCode) {
        try {
            log.debug("Parsing rate for currency: {}", currencyCode);
            return XmlCurrencyParser.getCurrencyRate(xml, currencyCode);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("Error parsing XML response for currency: {}", currencyCode, e);
            throw new ServiceException(ErrorType.BAD_REQUEST, "Error parsing XML response");
        } catch (IllegalArgumentException e) {
            log.error("Currency not found: {}", currencyCode, e);
            throw new ServiceException(ErrorType.CURRENCY_NOT_FOUND, "Currency not found: %s".formatted(currencyCode));
        }
    }
}
