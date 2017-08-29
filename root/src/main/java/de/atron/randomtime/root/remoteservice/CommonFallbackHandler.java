package de.atron.randomtime.root.remoteservice;

import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.ribbon.hystrix.FallbackHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import rx.Observable;

import java.util.Map;

public class CommonFallbackHandler implements FallbackHandler<ByteBuf> {

    @Override
    public Observable<ByteBuf> getFallback(HystrixInvokableInfo<?> hystrixInvokableInfo, Map<String, Object> map) {
        String fallback = "{ \"fallbacked\" : \"true\" }";
        byte[] bytes = fallback.getBytes();
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(bytes.length);
        byteBuf.writeBytes(byteBuf);
        return Observable.just(byteBuf);
    }

}
