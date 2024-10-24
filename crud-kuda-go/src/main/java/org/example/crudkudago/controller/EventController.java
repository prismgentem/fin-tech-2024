package org.example.crudkudago.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.crudkudago.model.EventRequest;
import org.example.crudkudago.model.EventResponse;
import org.example.crudkudago.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping()
    public ResponseEntity<List<EventResponse>> getAllEvents(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "fromDate", required = false) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) LocalDate toDate) {

        List<EventResponse> events = eventService.getAllEvents(name, place, fromDate, toDate);
        return ResponseEntity.ok(events);
    }
    @GetMapping ("/{id}")
    ResponseEntity<EventResponse> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PostMapping
    ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEventById(@PathVariable UUID id) {
        eventService.deleteEventById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<EventResponse> updateEventById(@PathVariable UUID id, @Valid @RequestBody EventRequest event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }
}
