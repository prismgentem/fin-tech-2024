package org.example.crudkudago.controller;

import lombok.RequiredArgsConstructor;
import org.example.crudkudago.model.KudaGoEventsResponse;
import org.example.crudkudago.service.EventService;
import org.example.logexecutionstarter.annotation.LogExecutionTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    @GetMapping("/events")
    public ResponseEntity<Mono<List<KudaGoEventsResponse>>> getEvents(
            @RequestParam Double budget,
            @RequestParam String currency,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        return ResponseEntity.ok(eventService.getFilteredEvents(budget, currency, dateFrom, dateTo));
    }
}
