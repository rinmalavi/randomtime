package de.atron.randomtime.root.remoteservice;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import org.jboss.logging.Logger;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;
import rx.Observable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TimeService {

    private final Logger log = Logger.getLogger(this.getClass());

    @Inject
    @ConfigurationValue("service.time.url")
    String timeServiceUrl;

    public Observable<ByteBuf> currentTime() {
        String getRequestUri = String.format("http://" + timeServiceUrl);
        log.infof("Time call to address: [%s]", getRequestUri);
        HttpClientRequest<ByteBuf> getRequest =
                HttpClientRequest
                .createGet(getRequestUri);
                //.withHeader("Accept", "*/*");
        Observable<ByteBuf> toGet = RxNetty.createHttpRequest(getRequest)
                .flatMap(response -> {
                    log.infof("Got response from time %s", response.getStatus().reasonPhrase());
                    return response.getContent();
                });
        return toGet;
    }

}