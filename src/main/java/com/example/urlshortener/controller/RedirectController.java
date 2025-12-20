package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;

    // 리다이렉트
    // 예시: GET / abc12345 -> 구글로 이동
    @GetMapping("/{shortId}")
    public ResponseEntity<Void> redirect(@PathVariable String shortId){
        String originalUrl = urlService.getOriginalUrl(shortId);

        if (originalUrl == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        urlService.increaseVisitCount(shortId);

        // 302 Found 상태코드와 Location 헤더를 사용하여 리다이렉트 시킴
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
