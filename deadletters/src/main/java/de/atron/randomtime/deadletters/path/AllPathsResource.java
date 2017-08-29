package de.atron.randomtime.deadletters.path;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

@RequestScoped
@Path("/")
public class AllPathsResource {

    private Logger log = Logger.getLogger(this.getClass());

    public AllPathsResource() {
        this.log.debug("Deadletters Paths ctor");
    }

    @GET
    @Path("{path : .*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpServletRequest httpServletRequest, @PathParam("path") String path) {
        this.log.infof("Dead letter arrived path: [%s]", path);

        return Response.ok()
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Credentials", "true")
            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, undefined")
            .header("Access-Control-Max-Age", "1209600")
            .build();
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        this.log.info("Dead letters  pre-flight");
        return Response.ok("")
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
            .header("Access-Control-Allow-Credentials", "true")
            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, undefined")
            .header("Access-Control-Max-Age", "1209600")
            .build();
    }

}
