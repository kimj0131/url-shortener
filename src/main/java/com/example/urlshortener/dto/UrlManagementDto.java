package com.example.urlshortener.dto;

import com.example.urlshortener.entity.UrlEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UrlManagementDto {
    private final Long id;
    private final String originalUrl;
    private final String shortId;
    private final long visitCount;
    private final LocalDateTime createdAt;

    public UrlManagementDto(UrlEntity entity) {
        this.id = entity.getUrlId();
        this.originalUrl = entity.getOriginalUrl();
        this.shortId = entity.getShortId();
        this.visitCount = entity.getVisitCount();
        this.createdAt = entity.getCreatedAt();
    }

    public static UrlManagementDto from(UrlEntity entity){
        return new UrlManagementDto(entity);
    }
}
