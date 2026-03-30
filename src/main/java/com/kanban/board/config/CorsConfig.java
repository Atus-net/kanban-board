package com.kanban.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng mở cửa cho toàn bộ các API bắt đầu bằng /api/...
                // Cho phép các domain của Frontend được phép gọi vào.
                // Nếu sau này bạn deploy Frontend lên domain thật (VD: kanban.com), thì thêm vào đây.
                .allowedOrigins("http://localhost:3000", "http://localhost:5173", "http://localhost:4200") 
                // Các hành động được phép
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Cho phép gửi kèm mọi loại Header (đặc biệt là cái Authorization chứa Token của mình)
                .allowedHeaders("*")
                // Cho phép gửi kèm cookie/thông tin xác thực
                .allowCredentials(true);
    }
}