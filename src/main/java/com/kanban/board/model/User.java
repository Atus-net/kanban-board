package com.kanban.board.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String firstName; 

    @Column(nullable = false)
    private String lastName;  

    private String avatarUrl;

    @CreationTimestamp // Tự động điền khi insert
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tự động cập nhật khi có bất kỳ thay đổi nào
    private LocalDateTime updatedAt;
}