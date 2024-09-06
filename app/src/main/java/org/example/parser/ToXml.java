package org.example.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ToXml {
    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ToXml.class);

    public static <T> Path toXml(T object, Path fileName) {
        LOGGER.debug("Start writing XML to file: {}", fileName);
        var appDir = Paths.get("").toAbsolutePath();
        var path = appDir.resolve(fileName);
        try {
            var xml = XML_MAPPER.writeValueAsString(object);
            Files.writeString(path, xml);
            LOGGER.info("Successfully written XML to file: {}", path);
            return path;
        } catch (IOException e) {
            LOGGER.error("Error while converting to XML and writing to file: {}", path, e);
            return null;
        } finally {
            LOGGER.debug("Complete writing XML to file: {}", fileName);
        }
    }
}
