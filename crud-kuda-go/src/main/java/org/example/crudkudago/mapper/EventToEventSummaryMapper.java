package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Event;
import org.example.crudkudago.model.EventSummary;
import org.springframework.core.convert.converter.Converter;

public class EventToEventSummaryMapper implements Converter<Event, EventSummary> {
    @Override
    public EventSummary convert(Event source) {
        return EventSummary.builder()
                .id(source.getId())
                .name(source.getName())
                .date(source.getDate().toString())
                .build();
    }
}
