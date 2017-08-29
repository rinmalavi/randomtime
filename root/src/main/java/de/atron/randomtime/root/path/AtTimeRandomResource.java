package de.atron.randomtime.root.path;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import de.atron.randomtime.root.remoteservice.RandomService;
import de.atron.randomtime.root.remoteservice.TimeService;
import de.atron.randomtime.root.model.AtRandomTime;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import org.jboss.logging.Logger;
import rx.Observable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RequestScoped
@Path("/")
public class AtTimeRandomResource {

    @Inject
    private TimeService timeService;

    @Inject
    private RandomService randomService;

    private final Logger log = Logger.getLogger(this.getClass());

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
    @Produces(MediaType.APPLICATION_JSON)
    public void get(@Context HttpServletRequest httpServletRequest, @Suspended final AsyncResponse asyncResponse) {
        log.info("Get to root (/)");
        Observable<ByteBuf> observableTime = this.timeService.currentTime();
        Observable<ByteBuf> observableRandomness = this.randomService.randomString(13);
        AtRandomTime atRandomTime = new AtRandomTime();

        Observable<String> afterTime = observableTime.map(
                (result) -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        ObjectReader reader = mapper.reader();
                        JsonFactory factory = new JsonFactory();
                        JsonParser parser = factory.createParser(new ByteBufInputStream(result));
                        Map map = reader.readValue(parser, Map.class);
                        log.infof("got response %s", map.get("time"));
                        CharSequence time = (String) map.get("time");
                        atRandomTime.setDateTime(LocalDateTime.parse(time));
                        return "given time " + time;
                    } catch (IOException e) {
                        log.errorf("error: %s" + e.getLocalizedMessage());
                        asyncResponse.resume(e);
                        return "no time";
                    }

                });

        Observable<String> afterRandom = observableRandomness.map(
                (result) -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        ObjectReader reader = mapper.reader();
                        JsonFactory factory = new JsonFactory();
                        JsonParser parser = factory.createParser(new ByteBufInputStream(result));
                        Map map = reader.readValue(parser, Map.class);
                        Object stringObj = map.get("string");
                        log.infof("got response form random %s", stringObj);
                        String string = (String) stringObj;
                        atRandomTime.setRandomString(string);
                        return "given string " + string;
                    } catch (IOException e) {
                        log.errorf("error: %s" + e.getLocalizedMessage());
                        asyncResponse.resume(e);
                        return "no string";
                    }

                });

        Observable.zip(afterTime, afterRandom,
                (result1, result2) -> {
                    log.infof("merge 2 responses %s %s", result1, result2);
                    asyncResponse.resume(atRandomTime);
                    return null;
                }
        ).subscribe();

    }
}
