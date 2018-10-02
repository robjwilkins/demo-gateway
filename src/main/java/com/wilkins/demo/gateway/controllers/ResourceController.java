package com.wilkins.demo.gateway.controllers;

import com.wilkins.demo.gateway.messaging.MessagingAdapter;
import com.wilkins.demo.gateway.model.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/resource")
@Slf4j
@RequiredArgsConstructor
public class ResourceController {

    private final MessagingAdapter messagingAdapter;

    @GetMapping
    public List<Resource> getResources() {
        log.info("getResources");
        Resource r1 = new Resource("r1", "demo resource");
        return Arrays.asList(r1);
    }

    @GetMapping("/{resourceId}")
    public Resource getResource(@PathVariable String resourceId) {
        log.info("getResource: {}", resourceId);
        String name = messagingAdapter.getResourceName(resourceId);
        return new Resource("r1", name);
    }

}
