package de.atron.randomtime.random.path;

import de.atron.randomtime.random.services.RandomProducer;
import de.atron.randomtime.random.StartupAchievement;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
@Path("")
public class RandomnessResource {

    public RandomnessResource() {
        log.debug( "TimeResource ctor" );
    }

    @Inject
    RandomProducer randomProducer;

    @Inject
    StartupAchievement startupAchievement;

    private Logger log = Logger.getLogger(this.getClass());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpServletRequest httpServletRequest) {
        log.infof("Asked for some randomness");
        String stringParam = httpServletRequest.getParameter("string");
        String intParam = httpServletRequest.getParameter("int");
        String boolParam = httpServletRequest.getParameter("bool");

        Map<String,Object> t = new HashMap<>();

        if (stringParam != null) {
            Integer toInt = 13;
            try {
                toInt = Integer.parseInt(stringParam);
            } catch (Exception e) {
                //do nothing - default
            }
            t.put("string", randomProducer.string(toInt));
        }
        if (intParam != null) {
            Integer toInt = 13;
            try {
                toInt = Integer.parseInt(intParam);
            } catch (Exception e) {
                //do nothing - default
            }
            t.put("int", randomProducer.integer(toInt));
        }
        if (boolParam != null)
            t.put("bool", randomProducer.bool());

        return Response.ok(t, MediaType.APPLICATION_JSON_TYPE)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, undefined")
                .header("Access-Control-Max-Age", "1209600")
                .entity(t)
                .build();
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        log.info( "Time pre-flight" );
        return Response.ok("")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, undefined")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }

}
