package com.example.urlshortener.controller;

import com.example.urlshortener.dto.CreateUrlDto;
import com.example.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class UrlViewController {

    private final UrlService urlService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/shorten")
    public String shorten(@Valid @ModelAttribute CreateUrlDto dto, Model model) {

        String shortId = urlService.shortenUrl(dto.getOriginalUrl());
        // 서버 기본주소 가져오기
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String resultUrl = baseUrl + "/" + shortId;
        model.addAttribute("shortUrl", resultUrl);

        return "index";
    }

    @GetMapping("/{shortUrl}")
    public String redirect(@PathVariable String shortId) {
        String originalUrl = urlService.getOriginalUrl(shortId);
        return "redirect:" + originalUrl;
    }

}
