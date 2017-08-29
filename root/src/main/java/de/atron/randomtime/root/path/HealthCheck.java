package de.atron.randomtime.root.path;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("/healthCheck")
public class HealthCheck {

    @GET
    public String healthCheck() {
        System.out.println("Health Check");

        return "alive";
    }

}
