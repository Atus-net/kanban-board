package com.kanban.board.config;

import lombok.RequiredArgsConstructor; // Thêm import này
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Thêm import này

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Thêm Annotation này để Inject Filter
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // Lắp trạm kiểm soát vào đây

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() 
                .anyRequest().authenticated()
            )
            // Gắn trạm kiểm soát của chúng ta vào trước trạm mặc định của Spring
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}