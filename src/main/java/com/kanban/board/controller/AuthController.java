package com.kanban.board.controller;

import com.kanban.board.model.User;
import com.kanban.board.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User savedUser = userService.registerUser(
                    request.getEmail(), 
                    request.getPassword(), 
                    request.getFullName()
            );
            return ResponseEntity.ok("Đăng ký thành công User: " + savedUser.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Class phụ để hứng dữ liệu JSON từ client gửi lên
    @Data
    static class RegisterRequest {
        private String email;
        private String password;
        private String fullName;
    }
}