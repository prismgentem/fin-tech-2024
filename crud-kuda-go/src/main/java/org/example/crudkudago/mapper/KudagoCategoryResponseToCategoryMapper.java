package org.example.crudkudago.mapper;

import org.example.crudkudago.entity.Category;
import org.example.crudkudago.model.KudagoCategoryResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KudagoCategoryResponseToCategoryMapper implements Converter<KudagoCategoryResponse, Category> {
    @Override
    public Category convert(KudagoCategoryResponse source) {
        return Category.builder()
                .slug(source.getSlug())
                .name(source.getName())
                .build();
    }
}
