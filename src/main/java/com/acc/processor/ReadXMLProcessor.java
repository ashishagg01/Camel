/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.acc.processor;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author ashish.g.agarwal
 * 
 *         processor to read xml string from message body and set it in headers
 */
public class ReadXMLProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(ReadXMLProcessor.class);

    @Override
    public void process(Exchange exchng) throws Exception {
        logger.debug("ReadXMLProcessor : started");
        try {
            String requestXml = exchng.getIn().getBody(String.class);
            XPath xpath = XPathFactory.newInstance().newXPath();
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(requestXml)));
            XPathExpression xpe = xpath.compile("count(//Params/param)");

            Double count = (Double) xpe.evaluate(doc, XPathConstants.NUMBER);
            logger.debug("Count of elements: " + count);

            for (int x = 1; x <= count; x++) {
                exchng.getIn().setHeader(xpath.compile("//param[" + x + "]/ParamName").evaluate(doc),
                        xpath.compile("//param[" + x + "]/ParamValue").evaluate(doc));

            }

        } catch (XPathExpressionException e) {
            exchng.getIn().setHeader("CamelFSLErrorReason", "XML Pasrsing Error");
            logger.debug("ReadXMLProcessor : finished");
            throw new ValidationException(exchng, "Validation failed");
        } catch (SAXException e) {
            exchng.getIn().setHeader("CamelFSLErrorReason", "XML Pasrsing Error");
            logger.debug("ReadXMLProcessor : finished");
            throw new ValidationException(exchng, "Validation failed");
        } catch (IOException e) {
            exchng.getIn().setHeader("CamelFSLErrorReason", "XML Pasrsing Error");
            logger.debug("ReadXMLProcessor : finished");
            throw new ValidationException(exchng, "Validation failed");
        } catch (ParserConfigurationException e) {
            exchng.getIn().setHeader("CamelFSLErrorReason", "XML Pasrsing Error");
            logger.debug("ReadXMLProcessor : finished");
            throw new ValidationException(exchng, "Validation failed");
        } catch (Exception e) {
            exchng.getIn().setHeader("CamelFSLErrorReason", "XML Pasrsing Error");
            logger.debug("ReadXMLProcessor : finished");
            throw new ValidationException(exchng, "Validation failed");
        }

    }
}
