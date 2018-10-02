package com.wilkins.demo.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class CacheConfig {

    private CacheManager cacheManager;

    private  CacheManager cacheManager() {
        if (cacheManager == null) {
            this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        }
        return cacheManager;
    }

    @Bean
    public Cache<String, String> responseCache() {
        String cacheName = "response-cache";
        return cacheManager().createCache(cacheName,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap(1000))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(60))).build());
    }
}
