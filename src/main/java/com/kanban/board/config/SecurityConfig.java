package com.kanban.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer; // Thêm import này
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // Thêm import này
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. MỞ KHÓA CORS (Để Frontend ở port khác có thể gọi API)
            .cors(Customizer.withDefaults()) 
            
            // 2. TẮT CSRF (Vì dùng API và JWT nên không cần chống tấn công CSRF theo kiểu cũ)
            .csrf(csrf -> csrf.disable())
            
            // 3. TẮT SESSION (Ép server chạy ở chế độ Stateless siêu nhẹ)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. PHÂN QUYỀN ĐƯỜNG DẪN
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Thả cửa cho Đăng ký / Đăng nhập
                .anyRequest().authenticated() // Bắt buộc xuất trình thẻ JWT cho mọi API khác
            )
            
            // 5. GẮN TRẠM KIỂM SOÁT JWT
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}