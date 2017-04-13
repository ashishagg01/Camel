/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acc.processor;

import java.io.FileNotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.language.simple.SimpleLanguage;
/**
 *
 * @author ashish.g.agarwal
 */
public class EnrichProcessor implements Processor {

    private static final Logger log = LoggerFactory.getLogger(EnrichProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        log.trace("started");

        Object fileName = exchange.getIn().getHeader("CamelEnrichResourcePath");
        if (fileName == null) {
            log.error("CamelEnrichResourcePath header is null");
        } else {
             log.trace("Resource file path:" + fileName.toString());
            Object resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName.toString());
            if (resource == null) {
                throw new FileNotFoundException();
            }
            String text = exchange.getContext().getTypeConverter().mandatoryConvertTo(String.class, resource);
            exchange.getIn().setBody(SimpleLanguage.simple(text, String.class).evaluate(exchange, Object.class));
         }
        log.trace("finished");
    }
}
