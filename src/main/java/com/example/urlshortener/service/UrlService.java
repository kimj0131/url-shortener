package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    @Transactional
    public String shortenUrl(String originalUrl) {
        String shortId;
        do {
            shortId = UUID.randomUUID().toString().substring(0, 8);
        } while (urlRepository.findByShortId(shortId).isPresent());

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        UrlEntity urlEntity = new UrlEntity(shortId, originalUrl);
        urlRepository.save(urlEntity);
        return shortId;
    }

    @Transactional
    public String getOriginalUrl(String shortId) {
        UrlEntity urlEntity = urlRepository.findByShortId(shortId)
                .orElseThrow(() -> new UrlNotFoundException("존재하지 않는 URL입니다."));

        increaseVisitCount(urlEntity);
        return urlEntity.getOriginalUrl();
    }

    private void increaseVisitCount(UrlEntity urlEntity) {
        urlEntity.increaseVisitCount();
    }
}
