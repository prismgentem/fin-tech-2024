package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Event;
import org.example.crudkudago.model.EventRequest;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventRequestToEventMapper implements Converter<EventRequest, Event> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public Event convert(EventRequest source) {
        return Event.builder()
                .name(source.getName())
                .date(LocalDate.parse(source.getDate(), DATE_FORMATTER))
                .build();
    }
}
