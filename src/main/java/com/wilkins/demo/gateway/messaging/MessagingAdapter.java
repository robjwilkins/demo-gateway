package com.wilkins.demo.gateway.messaging;


import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableBinding(MessagingChannels.class)
public class MessagingAdapter {

    private final MessageChannel demoRequestChannel;
    private final ResponseListener responseListener;
    private final Tracer tracer;

    public MessagingAdapter(@Qualifier(MessagingChannels.DEMO_REQUEST) MessageChannel demoRequestChannel,
                            ResponseListener responseListener, Tracer tracer) {
        this.demoRequestChannel = demoRequestChannel;
        this.responseListener = responseListener;
        this.tracer = tracer;
    }

    public String getResourceName(String resourceId) {
        Message<String> message = MessageBuilder.withPayload(resourceId).build();
        log.info("headers: {}", message.getHeaders());
        String traceId = tracer.currentSpan().context().traceIdString();
        log.info("traceId: {}", traceId);
        demoRequestChannel.send(message);
        return responseListener.getResponseForMessage(traceId);
    }

}
