package com.wilkins.demo.gateway.messaging;


import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableBinding(MessagingChannels.class)
public class ResourceManager {

    private final MessageChannel demoRequestChannel;
    private final ResponseListener responseListener;
    private final Tracer tracer;
    private final Cache<String, Boolean> traceIdCache;
    private final int instanceIndex;

    public ResourceManager(@Qualifier(MessagingChannels.DEMO_REQUEST) MessageChannel demoRequestChannel,
                           ResponseListener responseListener, Tracer tracer, Cache<String, Boolean> traceIdCache,
                           @Value("${spring.cloud.stream.instance-index}") int instanceIndex) {
        this.demoRequestChannel = demoRequestChannel;
        this.responseListener = responseListener;
        this.tracer = tracer;
        this.traceIdCache = traceIdCache;
        this.instanceIndex = instanceIndex;
    }

    public String getResourceName(String resourceId) {
        Message<String> message = MessageBuilder.withPayload(resourceId)
                .setHeader("response-partition-key", instanceIndex)
                .build();
        log.info("headers: {}", message.getHeaders());
        String traceId = tracer.currentSpan().context().traceIdString();
        log.info("traceId: {}", traceId);
        traceIdCache.put(traceId, true);
        demoRequestChannel.send(message);
        return responseListener.getResponseForMessage(traceId);
    }

}
