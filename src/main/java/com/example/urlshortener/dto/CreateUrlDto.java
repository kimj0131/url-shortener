package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlDto {

    @NotBlank(message = "URL을 입력해주세요.")
    @Pattern(regexp = "^(https?://)?([\\w-]+\\.)+[\\w-]+(/.*)?$", message = "올바른 URL 형식이 아닙니다.")
    private String originalUrl;
}
