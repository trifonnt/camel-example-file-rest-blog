package com.fusesource.samples;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * User: cposta
 */
public class CustomerEnricher implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		oldExchange.getIn().setHeader("OrigId", oldExchange.getIn().getBody(String.class));
		// oldExchange.getOut().setBody(newExchange.getIn().getBody(String.class));
		oldExchange.getIn().setBody(newExchange.getIn().getBody(String.class));
		return oldExchange;
	}
}