package com.kanban.board.service;

import com.kanban.board.model.User;
import com.kanban.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User registerUser(String email, String password, String fullName) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPasswordHash(passwordEncoder.encode(password));

        return userRepository.save(newUser);
    }
    public String login(String email, String password) {
        // 1. Tìm user trong Database theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        // 2. So sánh mật khẩu người dùng nhập với mật khẩu đã băm trong DB
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        // 3. Nếu đúng hết thì sinh ra JWT Token trả về
        return jwtService.generateToken(user);
    }
}