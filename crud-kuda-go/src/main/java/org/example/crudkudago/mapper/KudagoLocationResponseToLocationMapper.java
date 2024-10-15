package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Location;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KudagoLocationResponseToLocationMapper implements Converter<KudagoLocationResponse, Location>{
    @Override
    public Location convert(KudagoLocationResponse source) {
        return Location.builder()
                .slug(source.getSlug())
                .name(source.getName())
                .build();
    }
}
