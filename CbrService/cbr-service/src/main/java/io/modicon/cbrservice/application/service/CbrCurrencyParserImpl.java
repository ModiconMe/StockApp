package io.modicon.cbrservice.application.service;

import io.modicon.cbrservice.infrastructure.exception.ApiException;
import io.modicon.cbrservice.model.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class CbrCurrencyParserImpl implements CurrencyParser {
    @Override
    public Set<CurrencyRate> parse(String ratesAsString) {
        Set<CurrencyRate> currencyRates = new HashSet<>();

        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            try (var reader = new StringReader(ratesAsString)) {
                Document doc = db.parse(new InputSource(reader));
                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("Valute");

                for (var rowIdx = 0; rowIdx < list.getLength(); rowIdx++) {
                    var node = list.item(rowIdx);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        var element = (Element) node;
                        String name =  element.getElementsByTagName("Name").item(0).getTextContent();
                        String ticker =  element.getElementsByTagName("CharCode").item(0).getTextContent();
                        String rate =  element.getElementsByTagName("Value").item(0).getTextContent();
                        if(!ticker.isEmpty() && !rate.isEmpty()) {
                            currencyRates.add(new CurrencyRate(name, ticker, BigDecimal.valueOf(Double.parseDouble(rate.replace(",", ".")))));
                        }
                    }
                }
            }
            } catch (Exception ex) {
            log.error("xml parsing error, xml:{}", ratesAsString, ex);
            throw ApiException.exception(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return currencyRates;
    }
}
