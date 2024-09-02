package org.example.unit;

import org.example.model.City;
import org.example.model.Coords;
import org.example.parser.ToXml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ToXmlTest {
    private Path targetDir;

    @BeforeEach
    void setUp() throws IOException {
        targetDir = Paths.get("target");
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
    }

    @Test
    void testToXml() throws IOException {
        var city = new City("spb", new Coords(59.939095, 30.315868));
        var xmlFilePath = targetDir.resolve("city.xml");
        var path = ToXml.toXml(city, xmlFilePath);

        assertTrue(path != null);
        assertTrue(Files.exists(path));

        String xmlContent = Files.readString(path);

        assertTrue(xmlContent.contains("<slug>spb</slug>"));
        assertTrue(xmlContent.contains("<lat>59.939095</lat>"));
        assertTrue(xmlContent.contains("<lon>30.315868</lon>"));

    }
}
