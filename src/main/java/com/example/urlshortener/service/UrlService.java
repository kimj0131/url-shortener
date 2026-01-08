package com.example.urlshortener.service;

import com.example.urlshortener.dto.UrlCacheDto;
import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.entity.VisitHistoryEntity;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.repository.VisitHistoryRepository;
import com.example.urlshortener.util.Base62Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final VisitHistoryRepository visitHistoryRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public String shortenUrl(String originalUrl) {

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        Optional<UrlEntity> foundUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (foundUrl.isPresent()) {
            return foundUrl.get().getShortId();
        }

        UrlEntity urlEntity = new UrlEntity("", originalUrl);
        urlRepository.save(urlEntity);

        String shortId = Base62Util.encode(urlEntity.getUrlId());

        urlEntity.updateShortId(shortId);

        return shortId;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "urls", key = "#shortId")
    public UrlCacheDto getUrlCache(String shortId) {
        UrlEntity urlEntity = findUrlByShortId(shortId);

        return new UrlCacheDto(urlEntity.getOriginalUrl(), urlEntity.getUrlId());
    }

    @CacheEvict(value = "urls", key = "#shortId")
    public void deleteUrlCache(String shortId){
        log.info("Cache Evicted: {}", shortId);
    }

    @Transactional
    public void saveVisitHistory(UrlCacheDto dto, String clientId, String userAgent) {
//        log.info("current Thread: {}", Thread.currentThread().getName());

        UrlEntity urlEntity = urlRepository.getReferenceById(dto.getUrlId());

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
