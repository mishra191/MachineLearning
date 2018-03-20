/**
 * 
 */
package io.jta.examples;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;


/**
 * @author Anurag
 *
 */
public class BatchSizePredicate implements Predicate {

    public int size;

    public BatchSizePredicate(int size) {
            this.size = size;
    }

    @Override
    public boolean matches(Exchange exchange) {
            if (exchange != null) {
                    ArrayList list = exchange.getIn().getBody(ArrayList.class);
                    if (list.size() == size) {
                            return true;
                    }
            }
            return false;
    }

}