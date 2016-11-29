package com.fusesource.samples.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import com.fusesource.samples.CustomerEnricher;
import com.fusesource.samples.CustomerServiceProcessor;
import com.fusesource.samples.resource.CustomerServiceResource;

/**
 * User: cposta
 */
public class CustomerServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		getContext().setTracing(true);

		// from("cxfrs:bean:rsServer")
		from("cxfrs://http://localhost:9090/route?resourceClasses="+CustomerServiceResource.class.getName())
			.setHeader(Exchange.FILE_NAME, simple("test-${body}.xml"))
			.pollEnrich("file:src/data?noop=true", 1000, new CustomerEnricher())
//			.enrich("file:src/data?noop=false", new CustomerEnricher())
			.process(new CustomerServiceProcessor())
			.log("Here is the message that was enriched: ${body}");
	}
}