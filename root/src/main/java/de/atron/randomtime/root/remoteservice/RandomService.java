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
public class RandomService {

    private final Logger log = Logger.getLogger(this.getClass());

    @Inject
    @ConfigurationValue("service.random.url")
    String randomServiceUrl;

    public Observable<ByteBuf> randomString(int length) {
        String getRequestUri = String.format("http://%s?string=%d", randomServiceUrl, length);
        log.infof("Random String call to address: %s", getRequestUri);
        HttpClientRequest<ByteBuf> getRequest =
                HttpClientRequest
                        .createGet(getRequestUri);
                        //.withHeader("Accept", "*/*");
        Observable<ByteBuf> toGet = RxNetty.createHttpRequest(getRequest)
                .flatMap(response ->
                        response.getContent());
        return toGet;
    }

}