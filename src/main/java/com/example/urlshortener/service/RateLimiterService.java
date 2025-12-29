package com.example.urlshortener.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    public Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ip){
        return cache.computeIfAbsent(ip, key -> {
            // 규칙 - 1분에 10개씩 충전되고, 최대 10개까지 담을 수 있음
            Bandwidth limit = Bandwidth.builder()
                    .capacity(10)
                    .refillGreedy(10, Duration.ofMinutes(1))
                    .build();
            return Bucket.builder().addLimit(limit).build();
        });
    }

    public boolean tryConsumeToken(String ip){
        Bucket bucket = resolveBucket(ip);
        return bucket.tryConsume(1);
    }
}
