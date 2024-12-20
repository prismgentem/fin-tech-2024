package org.example.crudkudago.controller;

import lombok.RequiredArgsConstructor;
import org.example.crudkudago.model.KudaGoEventsResponse;
import org.example.crudkudago.service.EventServiceCompletableFuture;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class EventControllerCompletableFuture {
    private final EventServiceCompletableFuture eventServiceCompletableFuture;

    @GetMapping("/events-completable-future")
    public CompletableFuture<List<KudaGoEventsResponse>> getEvents(
            @RequestParam Double budget,
            @RequestParam String currency,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        return eventServiceCompletableFuture.getFilteredEvents(budget, currency, dateFrom, dateTo);
    }
}
