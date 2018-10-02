package com.wilkins.demo.gateway.messaging;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseListener {

    private final Tracer tracer;
    private final Cache<String, String> responseCache;

    @StreamListener(MessagingChannels.DEMO_RESPONSE)
    public void handleResponse(Message<String> message) {
        String traceId = tracer.currentSpan().context().traceIdString();
        log.info("Handling response message. traceId: {}, payload: {}", traceId, message.getPayload());
        responseCache.put(traceId, message.getPayload());
    }

    String getResponseForMessage(String key) {
        log.info("looking for key: {}", key);
        await().atMost(5, SECONDS).pollInterval(10, TimeUnit.MILLISECONDS).until(() -> responseCache.containsKey(key));
        String response = responseCache.get(key);
        responseCache.remove(key);
        log.info("returning response for key: {}", key);
        return response;
    }
}
