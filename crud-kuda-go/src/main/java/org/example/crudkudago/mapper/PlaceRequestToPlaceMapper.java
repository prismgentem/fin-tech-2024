package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Place;
import org.example.crudkudago.model.PlaceRequest;
import org.springframework.core.convert.converter.Converter;

public class PlaceRequestToPlaceMapper implements Converter<PlaceRequest, Place> {
    @Override
    public Place convert(PlaceRequest source) {
        return Place.builder()
                .name(source.getName())
                .city(source.getCity())
                .address(source.getAddress())
                .build();
    }
}
