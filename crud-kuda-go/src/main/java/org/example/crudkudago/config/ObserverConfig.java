package org.example.crudkudago.config;

import jakarta.annotation.PostConstruct;
import org.example.crudkudago.observer.SaveLogger;
import org.example.crudkudago.repository.LocationRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {

    private final LocationRepository locationRepository;
    private final SaveLogger saveLogger;

    public ObserverConfig(LocationRepository locationRepository, SaveLogger saveLogger) {
        this.locationRepository = locationRepository;
        this.saveLogger = saveLogger;
    }

    @PostConstruct
    public void init() {
        locationRepository.addObserver(saveLogger);
    }
}
