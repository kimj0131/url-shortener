package com.example.urlshortener.controller;

import com.example.urlshortener.dto.CreateUrlDto;
import com.example.urlshortener.service.UrlService;
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

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("createUrlDto", new CreateUrlDto());
        return "index";
    }

    @PostMapping("/shorten")
    public String shorten(@Valid @ModelAttribute CreateUrlDto dto,
                          BindingResult bindingResult,
                          Model model) {
        // 유효성 검사 실패 시
        if(bindingResult.hasErrors()) {
            return "index";
        }

        String shortId = urlService.shortenUrl(dto.getOriginalUrl());
        // 서버 기본주소 가져오기
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String resultUrl = baseUrl + "/" + shortId;
        model.addAttribute("shortUrl", resultUrl);

        return "index";
    }

}
