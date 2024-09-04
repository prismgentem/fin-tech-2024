package org.example.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class JsonParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> Optional<T> parseFromJson(Path path, Class<T> clazz) {
        LOGGER.debug("Start parsing JSON file '{}'", path);
        var jsonFile = path.toFile();
        try {
            T result = MAPPER.readValue(jsonFile, clazz);
            return Optional.ofNullable(result);
        } catch (IOException e) {
            LOGGER.error("Failed to parse JSON file '{}': {}", path, e.toString());
            return Optional.empty();
        } finally {
            LOGGER.debug("Complete parsing JSON file '{}'", path);
        }
    }
}
