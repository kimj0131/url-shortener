package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class CreateUrlDto {

    @NotBlank
    @URL
    private String originalUrl;
}
