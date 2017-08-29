package de.atron.randomtime.time.path;

import de.atron.randomtime.time.StartupAchievement;
import de.atron.randomtime.time.model.TimeQuest;
import de.atron.randomtime.time.services.TimeQuestRepository;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
@Path("")
public class TimeResource {

    public TimeResource() {
        log.debug( "TimeResource ctor" );
    }

    @Inject
    TimeQuestRepository timeQuestRepository;

    @Inject
    StartupAchievement startupAchievement;

    private Logger log = Logger.getLogger(this.getClass());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpServletRequest httpServletRequest) {
        log.infof("Asked for the time");
        Map<String,Object> t = new HashMap<>();
        LocalDateTime d = LocalDateTime.now();

        timeQuestRepository.saveTime(d);

        t.put( "time", d.toString() );

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

    @GET
    @Path("/all")
    @Produces("application/json")
    public TimeQuest[] all() {
        return timeQuestRepository.getTimeQuests();
    }

}
