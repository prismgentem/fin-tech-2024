package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Place;
import org.example.crudkudago.model.PlaceSummary;
import org.springframework.core.convert.converter.Converter;

public class PlaceToPlaceSummaryMapper implements Converter<Place, PlaceSummary> {
    @Override
    public PlaceSummary convert(Place source) {
        return PlaceSummary.builder()
                .id(source.getId())
                .name(source.getName())
                .address(source.getAddress())
                .city(source.getCity())
                .build();
    }
}
