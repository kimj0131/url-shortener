package com.example.urlshortener.service;

import com.example.urlshortener.dto.UrlManagementDto;
import com.example.urlshortener.entity.UrlEntity;
import com.example.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UrlRepository urlRepository;
    private final UrlService urlService;

    @Transactional(readOnly = true)
    public Page<UrlManagementDto> findAllUrls(Pageable pageable) {

        Page<UrlEntity> urlPage = urlRepository.findAll(pageable);

        return urlPage.map(UrlManagementDto::from);
    }

    @Transactional
    public void deleteUrl(Long id){
        UrlEntity url = urlRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 URL이 존재하지 않습니다. id=" + id)
        );

        urlService.deleteUrlCache(url.getShortId());

        urlRepository.delete(url);
    }
}
