package com.kanban.board.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "boards")
public class Board {
    
    @Id
    private String id; // MongoDB tự động sinh ID dạng String (ObjectId)

    // Sợi dây liên kết với PostgreSQL: Lưu ID của Workspace
    private String workspaceId; 
    
    private String title;
    private String visibility = "WORKSPACE"; // PUBLIC, WORKSPACE, PRIVATE

    // ĐIỂM ĂN TIỀN CỦA MONGODB: Nhúng (Embed) luôn danh sách Cột vào trong Board
    private List<ListConfig> lists = new ArrayList<>();
    
    private LocalDateTime createdAt = LocalDateTime.now();

    // Class cấu hình cho 1 Cột (Ví dụ: Cột To-do, In Progress...)
    @Data
    public static class ListConfig {
        private String id = UUID.randomUUID().toString(); // Tự sinh ID cho Cột
        private String title;
        private Integer order; // Dùng để sắp xếp vị trí cột khi kéo thả
    }
}