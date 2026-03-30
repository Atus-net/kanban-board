package com.kanban.board.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
@Validated // Bắt buộc Spring Boot phải soi kỹ các điều kiện bên dưới
@Data
public class JwtConfig {

    @NotBlank(message = "CẢNH BÁO CRITICAL: Thiếu biến môi trường JWT_SECRET_KEY!")
    private String secretKey;

    @Min(value = 3600000, message = "CẢNH BÁO: Token expiration phải từ 1 tiếng trở lên!")
    private long expiration;
}