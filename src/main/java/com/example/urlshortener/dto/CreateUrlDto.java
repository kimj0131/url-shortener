package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class CreateUrlDto {

    @NotBlank
    @URL
    private String originalUrl;
}
