package org.acme.resource;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/greetings")
public class GreetingsResource {

    @ConfigProperty(name = "greeting.message")
    String greetings;

    @Inject
    Logger logger;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        logger.info(greetings);
        return "hello " + greetings;
    }

}
