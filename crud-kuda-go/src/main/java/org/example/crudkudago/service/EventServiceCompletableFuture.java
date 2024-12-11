package org.example.crudkudago.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.model.ConvertRequest;
import org.example.crudkudago.model.KudaGoEventsResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceCompletableFuture {

    private final KudagoClient kudagoClient;
    private final CurrencyService currencyService;
    private final ExecutorService fixedThreadPool;

    public CompletableFuture<List<KudaGoEventsResponse>> getFilteredEvents(
            double budget, String currency, LocalDate dateFrom, LocalDate dateTo) {

        var startDate = (dateFrom != null) ? dateFrom : LocalDate.now();
        var endDate = (dateTo != null) ? dateTo : LocalDate.now().plusDays(7);

        CompletableFuture<List<KudaGoEventsResponse>> eventsFuture = CompletableFuture.supplyAsync(() ->
                        kudagoClient.getEvents(startDate, endDate)
                                .collectList()
                                .block(),
                fixedThreadPool);

        var convertRequest = ConvertRequest.builder()
                .fromCurrency(currency.toUpperCase())
                .toCurrency("RUB")
                .amount(budget)
                .build();

        CompletableFuture<Double> convertedBudgetFuture = CompletableFuture.supplyAsync(() -> {
            var response = currencyService.convert(convertRequest);
            return response.getConvertedAmount();
        }, fixedThreadPool);

        CompletableFuture<List<KudaGoEventsResponse>> resultFuture = new CompletableFuture<>();

        eventsFuture.thenAcceptBoth(convertedBudgetFuture, (events, convertedBudget) -> {
            List<KudaGoEventsResponse> filteredEvents = events.stream()
                    .filter(event -> isEventWithinBudget(event, convertedBudget))
                    .collect(Collectors.toList());
            resultFuture.complete(filteredEvents);
        }).exceptionally(ex -> {
            resultFuture.completeExceptionally(ex);
            return null;
        });

        return resultFuture;
    }

    private boolean isEventWithinBudget(KudaGoEventsResponse event, double convertedBudget) {
        if (event.getPrice() == null || event.getPrice().isEmpty()) {
            return true;
        }

        try {
            double eventPrice = Double.parseDouble(event.getPrice());
            return eventPrice <= convertedBudget;
        } catch (NumberFormatException e) {
            log.error("Invalid event price: {}", event.getPrice());
            return false;
        }
    }
}
