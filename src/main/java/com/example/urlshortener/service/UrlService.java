package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.entity.VisitHistoryEntity;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.repository.VisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final VisitHistoryRepository visitHistoryRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public String shortenUrl(String originalUrl) {
        String shortId;
        do {
            shortId = UUID.randomUUID().toString().substring(0, 8);
        } while (urlRepository.findByShortId(shortId).isPresent());

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        Optional<UrlEntity> fountUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (fountUrl.isPresent()) {
            return fountUrl.get().getShortId();
        }

        UrlEntity urlEntity = new UrlEntity(shortId, originalUrl);
        urlRepository.save(urlEntity);
        return shortId;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "urls", key = "#shortId")
    public String getOriginalUrl(String shortId) {
        UrlEntity urlEntity = findUrlByShortId(shortId);

        return urlEntity.getOriginalUrl();
    }

    @Async
    @Transactional
    public void saveVisitHistory(String shortId, String clientId, String userAgent) {
        log.info("current Thread: {}", Thread.currentThread().getName());

        UrlEntity urlEntity = findUrlByShortId(shortId);

        VisitHistoryEntity visitHistoryEntity = new VisitHistoryEntity(urlEntity, clientId, userAgent);

        visitHistoryRepository.save(visitHistoryEntity);
    }

    @Transactional(readOnly = true)
    public UrlEntity findUrlByShortId(String shortId) {
        return urlRepository.findByShortId(shortId)
                .orElseThrow(() -> new UrlNotFoundException("존재하지 않는 URL입니다."));
    }

    public void increaseVisitCount(String shortId) {
        String key = "visitCount:" + shortId;
        redisTemplate.opsForValue().increment(key);
    }
}
