package com.kanban.board.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Data
@Entity
@Table(name = "workspaces")
public class Workspace {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    // Lưu ID của người tạo (Owner)
    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @CreationTimestamp // Tự động điền khi insert
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Tự động cập nhật khi có bất kỳ thay đổi nào
    private LocalDateTime updatedAt;
}