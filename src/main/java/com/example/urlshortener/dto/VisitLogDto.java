package com.example.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VisitLogDto {
    private UrlCacheDto urlCacheDto;
    private String clientIp;
    private String userAgent;

    public static VisitLogDto createDto(UrlCacheDto urlCacheDto, String clientIp, String userAgent){
        return new VisitLogDto(urlCacheDto, clientIp, userAgent);
    }
}
