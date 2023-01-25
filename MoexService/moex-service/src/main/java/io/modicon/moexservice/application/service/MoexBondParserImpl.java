package io.modicon.moexservice.application.service;

import io.modicon.moexservice.domain.model.Bond;
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
import java.util.ArrayList;
import java.util.List;

import static io.modicon.moexservice.infrastructure.exception.ApiException.exception;

@Slf4j
@Service
public class MoexBondParserImpl implements BondParser {
    @Override
    public List<Bond> parse(String ratesAsString) {
        List<Bond> bonds = new ArrayList<>();

        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            try (var reader = new StringReader(ratesAsString)) {
                Document doc = db.parse(new InputSource(reader));
                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("row");

                for (var rowIdx = 0; rowIdx < list.getLength(); rowIdx++) {
                    var node = list.item(rowIdx);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        var element = (Element) node;
                        String ticker =  element.getAttribute("SECID");
                        String price = element.getAttribute("PREVADMITTEDQUOTE");
                        String name = element.getAttribute("SHORTNAME");
                        if(!ticker.isEmpty() && !price.isEmpty() && !name.isEmpty()) {
                            bonds.add(new Bond(ticker, BigDecimal.valueOf(Double.parseDouble(price)), name));
                        }
                    }
                }
            }
            } catch (Exception ex) {
            log.error("xml parsing error, xml:{}", ratesAsString, ex);
            throw exception(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return bonds;
    }
}
