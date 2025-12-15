package com.example.urlshortener.controller;

import com.example.urlshortener.dto.CreateUrlDto;
import com.example.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UrlViewController {

    private final UrlService urlService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @PostMapping("/shorten")
    public String shorten(@ModelAttribute CreateUrlDto dto, Model model){

        String shortUrl = urlService.shortenUrl(dto.getOriginalUrl());
        model.addAttribute("shortUrl", shortUrl);

        return "index";
    }

    @GetMapping
    public String redirect(@PathVariable String shortId) {
        String originalUrl = urlService.getOriginalUrl(shortId);
        return "redirect:" + originalUrl;
    }

}
