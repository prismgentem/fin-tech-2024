package org.example.crudkudago.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.client.KudagoClient;
import org.example.crudkudago.entity.Location;
import org.example.crudkudago.model.KudagoLocationResponse;
import org.example.crudkudago.service.LocationService;
import org.example.logexecutionstarter.annotation.LogExecutionTime;
import org.springframework.core.convert.ConversionService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class InitLocationsCommand implements Command {

    private final LocationService locationService;
    private final KudagoClient kudagoClient;
    private final ConversionService conversionService;

    @LogExecutionTime
    @Override
    public void execute() {
        log.info("Initializing locations...");
        List<KudagoLocationResponse> locations = kudagoClient.fetchLocations();
        locations.forEach(locationResponse -> {
            var location = conversionService.convert(locationResponse, Location.class);
            locationService.createLocation(location);
        });
        log.info("Locations initialized. Total: {}", locations.size());
    }
}
