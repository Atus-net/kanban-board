package com.kanban.board.service;

import com.kanban.board.model.User;
import com.kanban.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Danh sách màu sắc ngẫu nhiên cho Avatar nếu User không có ảnh
    private final String[] defaultColors = {"#FF5733", "#33FF57", "#3357FF", "#F333FF", "#FF33A8", "#33FFF5"};

    public User registerUser(String email, String password, String name) {
        // 1. Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // 2. Khởi tạo User mới với các trường đã update
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name); // Sử dụng trường name mới
        
        // Gán một màu ngẫu nhiên cho avatar
        newUser.setColor(defaultColors[new Random().nextInt(defaultColors.length)]);
        
        // Mã hóa mật khẩu vào trường password mới
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setIsActive(true);

        return userRepository.save(newUser);
    }

    public String login(String email, String password) {
        // 1. Tìm user trong Database theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        // 2. So sánh mật khẩu (Sử dụng trường password mới)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        // 3. Sinh JWT Token
        return jwtService.generateToken(user);
    }
}