package com.kanban.board.config;

import com.kanban.board.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. Lấy thông tin Header mang tên "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Kiểm tra xem Header có chứa Token chuẩn (bắt đầu bằng "Bearer ") không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Không có thì bỏ qua, đi tiếp (sẽ bị chặn ở SecurityConfig)
            return;
        }

        // 3. Cắt lấy cái chuỗi mã hóa (Bỏ chữ "Bearer " đi)
        jwt = authHeader.substring(7);
        
        try {
            // 4. Giải mã Token lấy email
            userEmail = jwtService.extractUsername(jwt);

            // 5. Nếu có email và trong SecurityContext chưa có ai đăng nhập
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Xác nhận người dùng hợp lệ và set quyền (hiện tại để trống ArrayList vì chưa làm Role)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, new ArrayList<>()
                );
                // Lưu vào hệ thống an ninh của Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Token hết hạn hoặc bị sai chữ ký
            System.out.println("Token không hợp lệ: " + e.getMessage());
        }

        // Đi tiếp vào Controller
        filterChain.doFilter(request, response);
    }
}