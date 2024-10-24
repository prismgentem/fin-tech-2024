package org.example.crudkudago.service;

import lombok.RequiredArgsConstructor;
import org.example.crudkudago.entity.Event;
import org.example.crudkudago.entity.Place;
import org.example.crudkudago.exception.ErrorType;
import org.example.crudkudago.exception.ServiceException;
import org.example.crudkudago.model.EventRequest;
import org.example.crudkudago.model.EventResponse;
import org.example.crudkudago.model.PlaceSummary;
import org.example.crudkudago.repository.EventRepository;
import org.example.crudkudago.repository.PlaceRepository;
import org.example.crudkudago.util.EventSpecification;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    public static final String MSG_PLACE_NOT_EXIST = "Place with id %s does not exist.";
    public static final String MSG_EVENT_NOT_FOUND = "Event with id %s not found.";

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;
    private final ConversionService conversionService;

    @Transactional
    public EventResponse createEvent(EventRequest request) {
        var place = validateAndGetPlace(request.getPlaceId());

        var event = conversionService.convert(request, Event.class);
        event.setPlace(place);

        return conversionService.convert(eventRepository.save(event), EventResponse.class);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID id) {
        var event = validateAndGetEvent(id);

        return conversionService.convert(event, EventResponse.class);
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAllEvents(String name, String place, LocalDate fromDate, LocalDate toDate) {
        var specification = EventSpecification.filterEvents(name, place, fromDate, toDate);
        var events = eventRepository.findAll(specification);

        return events.stream()
                .map(event -> {
                    var eventResponse = conversionService.convert(event, EventResponse.class);
                    if (eventResponse != null) {
                        eventResponse.setPlace(conversionService.convert(event.getPlace(), PlaceSummary.class));
                    }
                    return eventResponse;
                })
                .toList();
    }

    @Transactional
    public void deleteEventById(UUID id) {
        validateAndGetEvent(id);
        eventRepository.deleteById(id);
    }

    @Transactional
    public EventResponse updateEvent(UUID id, EventRequest eventRequest) {
        var event = validateAndGetEvent(id);
        var place = validateAndGetPlace(eventRequest.getPlaceId());
        var updatedEvent = conversionService.convert(eventRequest, Event.class);
        event.setPlace(place);
        event.setName(updatedEvent.getName());
        event.setDate(updatedEvent.getDate());
        return conversionService.convert(eventRepository.save(event), EventResponse.class);
    }

    private Event validateAndGetEvent(UUID id) {
        var event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new ServiceException(ErrorType.NOT_FOUND, MSG_EVENT_NOT_FOUND, id);
        }
        return event.get();
    }

    private Place validateAndGetPlace(UUID id) {
        var place = placeRepository.findById(id);
        if (place.isEmpty()) {
            throw new ServiceException(ErrorType.BAD_REQUEST, MSG_PLACE_NOT_EXIST, id);
        }
        return place.get();
    }
}
