package com.kanban.board.service;

import com.kanban.board.model.User;
import com.kanban.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(String email, String password, String fullName) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        // Tạm thời lưu plain text, sẽ cập nhật mã hóa (BCrypt) sau
        newUser.setPasswordHash(password); 

        return userRepository.save(newUser);
    }
}