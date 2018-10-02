package com.wilkins.demo.gateway.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessagingChannels {

    String DEMO_REQUEST = "demo-request";
    String DEMO_RESPONSE = "demo-response";

    @Output(DEMO_REQUEST)
    MessageChannel demoRequest();

    @Input(DEMO_RESPONSE)
    SubscribableChannel demoResponse();
}
