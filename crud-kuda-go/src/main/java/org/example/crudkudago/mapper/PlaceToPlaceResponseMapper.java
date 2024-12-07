package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Place;
import org.example.crudkudago.model.PlaceRequest;
import org.example.crudkudago.model.PlaceResponse;
import org.springframework.core.convert.converter.Converter;

public class PlaceToPlaceResponseMapper implements Converter<Place, PlaceResponse> {
    @Override
    public PlaceResponse convert(Place source) {
        return PlaceResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .address(source.getAddress())
                .city(source.getCity())
                .build();
    }
}
