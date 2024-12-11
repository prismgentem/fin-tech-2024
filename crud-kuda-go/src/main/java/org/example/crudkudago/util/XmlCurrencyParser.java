package org.example.crudkudago.util;

import lombok.experimental.UtilityClass;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

@UtilityClass
public class XmlCurrencyParser {

    public static Double getCurrencyRate(String xml, String currencyCode) throws ParserConfigurationException, IOException, SAXException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var doc = builder.parse(new InputSource(new StringReader(xml)));

        var currencies = doc.getElementsByTagName("Valute");
        for (int i = 0; i < currencies.getLength(); i++) {
            var currency = (Element) currencies.item(i);
            var code = currency.getElementsByTagName("CharCode").item(0).getTextContent();
            if (code.equals(currencyCode)) {
                var rate = currency.getElementsByTagName("Value").item(0).getTextContent();
                return Double.parseDouble(rate.replace(",", "."));
            }
        }
        throw new IllegalArgumentException("Currency code not found: " + currencyCode);
    }
}
