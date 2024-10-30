package org.example.crudkudago.observer;

import org.example.crudkudago.entity.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaveLogger implements Observer<Location> {
    private static final Logger logger = LoggerFactory.getLogger(SaveLogger.class);

    @Override
    public void onSave(Location entity) {
        logger.info("Entity saved: " + entity);
    }

    @Override
    public void onDelete(Location entity) {
        logger.info("Entity deleted: " + entity);
    }
}
