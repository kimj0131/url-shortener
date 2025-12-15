package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
public class UrlController {

    private final UrlService urlService;

    // API 1: 단축 URL 생성
    // 예시: POST /api/shorten?url=https://www.google.com
    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestParam @NotBlank @URL String url){

        String shortId = urlService.shortenUrl(url);

        // 실제 서비스라면 도메인을 붙여서 리턴 (예: http://localhost:8080/abc12345)
        return ResponseEntity.ok(shortId);
    }

    // API 2: 리다이렉트
    // 예시: GET / abc12345 -> 구글로 이동
    @GetMapping("/{shortId}")
    public ResponseEntity<Void> redirect(@PathVariable String shortId){
        String originalUrl = urlService.getOriginalUrl(shortId);

        if (originalUrl == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 302 Found 상태코드와 Location 헤더를 사용하여 리다이렉트 시킴
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
