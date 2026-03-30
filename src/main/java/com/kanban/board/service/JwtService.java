package com.kanban.board.service;

import com.kanban.board.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Chữ ký bí mật của Server (thực tế sẽ lưu trong application.yml để bảo mật)
    // Đây là một chuỗi ngẫu nhiên đã được mã hóa Base64 (phải dài ít nhất 256-bit)
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // Hàm tạo Token khi user đăng nhập thành công
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Thông tin định danh chính
                .claim("userId", user.getId()) // Nhét thêm ID vào payload để sau này lấy cho lẹ
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token sống được 24 giờ
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký tên bằng thuật toán HS256
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
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}