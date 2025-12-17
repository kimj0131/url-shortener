package com.example.urlshortener.dto;

import com.example.urlshortener.entity.UrlEntity;
import lombok.Getter;

@Getter
public class UrlResponseDto {

    private final String originalUrl;
    private final String shortUrl;
    private final Long visitCount;

    private UrlResponseDto(String originalUrl, String shortUrl, Long visitCount) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.visitCount = visitCount;
    }

    public static UrlResponseDto from(UrlEntity entity){
        return new UrlResponseDto(
                entity.getOriginalUrl(),
                entity.getShortId(),
                entity.getVisitCount());
    }

}
