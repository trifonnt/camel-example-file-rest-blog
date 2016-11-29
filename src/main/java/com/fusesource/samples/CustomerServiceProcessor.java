package com.fusesource.samples;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fusesource.samples.model.Customer;
import com.fusesource.samples.model.Order;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * User: cposta
 */
public class CustomerServiceProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceProcessor.class);

	private Map<Long, Customer> customers = new HashMap<Long, Customer>();

	private Map<Long, Order> orders = new HashMap<Long, Order>();


	public CustomerServiceProcessor() {
		init();
	}

	final void init() {
		Customer c = new Customer();
		c.setName("John");
		c.setId(123);
		customers.put(c.getId(), c);

		Order o = new Order();
		o.setDescription("order 223");
		o.setId(223);
		orders.put(o.getId(), o);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		String operationName = inMessage.getHeader(CxfConstants.OPERATION_NAME, String.class);
		if ("getCustomer".equalsIgnoreCase(operationName)) {
			String id = inMessage.getBody(String.class);
//			String id = inMessage.getHeader("OrigId", String.class);
			LOG.info("----invoking getCustomer, Customer id is: " + id);

			long idNumber = Long.parseLong(id);

			Customer customer = customers.get(idNumber);

			if (customer == null) {
				System.out.println("Customer is null: " + (customer == null));
				Response response = Response.status(Response.Status.BAD_REQUEST)
						.entity("<error>could not find customer</error>").build();
				exchange.getOut().setBody(response);
			}
			exchange.getOut().setBody(customer);
		}
	}
}