package org.example.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.City;
import org.example.parser.JsonParser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonParserTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParserTest.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Path TEST_JSON_PATH = Paths.get("src/test/resources/city.json");
    @Test
     void testParseJson() {

        Optional<City> parsedJson = JsonParser.parseFromJson(TEST_JSON_PATH, City.class);
        assertTrue(parsedJson.isPresent());

        City city = parsedJson.get();
        assertTrue(city.getSlug().equals("spb"));
        assertTrue(city.getCoords().getLat().equals(59.939095));
        assertTrue(city.getCoords().getLon().equals(30.315868));
    }
}
