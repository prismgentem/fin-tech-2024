package org.example.crudkudago.config;

import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.command.InitLocationsCommand;
import org.example.crudkudago.command.InitCategoriesCommand;
import org.example.crudkudago.service.LocationService;
import org.example.crudkudago.service.CategoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

@Configuration
public class CommandConfig {

    @Bean
    public InitCategoriesCommand initCategoriesCommand(CategoryService categoryService,
                                                       KudagoClient kudagoClient,
                                                       ConversionService conversionService) {
        return new InitCategoriesCommand(categoryService, kudagoClient, conversionService);
    }

    @Bean
    public InitLocationsCommand initLocationsCommand(LocationService locationService,
                                                     KudagoClient kudagoClient,
                                                     ConversionService conversionService) {
        return new InitLocationsCommand(locationService, kudagoClient, conversionService);
    }
}
