package com.kanban.board.service;

import com.kanban.board.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; // Thêm import này
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Kéo cấu hình SECRET_KEY từ application.yml (thực chất là từ biến môi trường của hệ thống)
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // Kéo cấu hình thời gian sống của Token từ application.yml
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // Hàm tạo Token khi user đăng nhập thành công
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) 
                .claim("userId", user.getId()) 
                .setIssuedAt(new Date(System.currentTimeMillis())) 
                // Thay vì hardcode số, giờ mình dùng biến jwtExpiration
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) 
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) 
                .compact();
    }
    
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private Key getSignInKey() {
        // Dùng biến secretKey thay vì chuỗi cố định
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}