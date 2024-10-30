package org.example.crudkudago.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crudkudago.command.Command;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    private final Command initCategoriesCommand;
    private final Command initLocationsCommand;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Starting data initialization after application ready...");
        initCategoriesCommand.execute();
        initLocationsCommand.execute();
        log.info("Completed data initialization.");
    }
}
