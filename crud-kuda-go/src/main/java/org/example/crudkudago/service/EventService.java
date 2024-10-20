package org.example.crudkudago.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.model.ConvertRequest;
import org.example.crudkudago.model.KudaGoEventsResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final KudagoClient kudagoClient;
    private final CurrencyService currencyService;

    public Mono<List<KudaGoEventsResponse>> getFilteredEvents(
            double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {

        var startDate = (dateFrom != null) ? dateFrom : LocalDate.now();
        var endDate = (dateTo != null) ? dateTo : LocalDate.now().plusDays(7);

        var convertRequest = ConvertRequest.builder()
                .fromCurrency(currency.toUpperCase())
                .toCurrency("RUB")
                .amount(budget)
                .build();

        Flux<KudaGoEventsResponse> eventsFlux = kudagoClient.getEvents(startDate, endDate);

        Mono<Double> convertedBudgetMono = Mono.fromCallable(() -> {
            var response = currencyService.convert(convertRequest);
            return response.getConvertedAmount();
        });

        return convertedBudgetMono.flatMapMany(convertedBudget ->
                        eventsFlux.filter(event -> isEventWithinBudget(event, convertedBudget))
                )
                .collectList();
    }

    private boolean isEventWithinBudget(KudaGoEventsResponse event, double convertedBudget) {
        if (event.getPrice() == null || event.getPrice().isEmpty()) {
            return true;
        }

        try {
            double eventPrice = extractMinimumPrice(event.getPrice());
            return eventPrice <= convertedBudget;
        } catch (NumberFormatException e) {
            log.error("Invalid event price: {}", event.getPrice());
            return false;
        }
    }

    private double extractMinimumPrice(String price) throws NumberFormatException {
        var pattern = Pattern.compile("\\d+(?:[,.]\\d+)?");
        var matcher = pattern.matcher(price);

        double minPrice = Double.MAX_VALUE;
        while (matcher.find()) {
            var priceStr = matcher.group().replace(",", ".");
            double currentPrice = Double.parseDouble(priceStr);
            minPrice = Math.min(minPrice, currentPrice); // Ищем минимальную цену
        }

        if (minPrice == Double.MAX_VALUE) {
            throw new NumberFormatException("No valid price found in string: " + price);
        }

        return minPrice;
    }

}
