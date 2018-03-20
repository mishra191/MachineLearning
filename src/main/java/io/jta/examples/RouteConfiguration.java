/**
 * 
 */
package io.jta.examples;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Anurag
 *
 */
public class RouteConfiguration extends CamelConfiguration {
    @Autowired
    LogRoute logRoute;

    @Override
    public List routes() {
            List routeBuilders = new ArrayList();
            routeBuilders.add(logRoute);
            return routeBuilders;
    }

}
