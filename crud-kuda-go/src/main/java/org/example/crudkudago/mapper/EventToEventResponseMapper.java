package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Event;
import org.example.crudkudago.model.EventResponse;
import org.springframework.core.convert.converter.Converter;

public class EventToEventResponseMapper implements Converter<Event, EventResponse> {
    @Override
    public EventResponse convert(Event source) {
        return EventResponse.builder()
                .id(source.getId())
                .date(source.getDate().toString())
                .name(source.getName())
                .build();
    }
}
