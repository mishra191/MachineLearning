/**
 * 
 */
package io.jta.examples;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @author Anurag
 *
 */
public class LoginProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("Received Order: " +
				exchange.getIn().getBody(String.class));
		
	}

}
