package com.example.urlshortener.controller;

import com.example.urlshortener.dto.CreateUrlDto;
import com.example.urlshortener.service.RateLimiterService;
import com.example.urlshortener.service.UrlService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class UrlViewController {

    private final UrlService urlService;
    private final RateLimiterService rateLimiterService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("createUrlDto", new CreateUrlDto());
        return "index";
    }

    @PostMapping("/shorten")
    public String shorten(@Valid @ModelAttribute CreateUrlDto dto,
                          BindingResult bindingResult,
                          HttpServletRequest request,
                          Model model) {
        // 유효성 검사 실패 시
        if(bindingResult.hasErrors()) {
            return "index";
        }

        // Rate Limiting 검사
        if(!rateLimiterService.tryConsumeToken(request.getRemoteAddr())) {
            // 실패 시 에러 플래그 전달
            model.addAttribute("rateLimitError", "true");
            return "index";
        }

        String shortId = urlService.shortenUrl(dto.getOriginalUrl());
        model.addAttribute("shortUrl", buildFullUrl(shortId));

        return "index";
    }

    private String buildFullUrl(String shortId) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return baseUrl + "/" + shortId;
    }

}
