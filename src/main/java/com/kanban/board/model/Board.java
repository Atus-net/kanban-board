package com.kanban.board.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Document(collection = "boards") // Đánh dấu lưu trữ tại MongoDB
public class Board {
    @Id
    private String id;
    private String workspaceId; // Khóa ngoại nối sang ID của Workspace bên Postgres
    private String title;
    private String coverImage;

    // Cấu trúc Map để lưu trữ linh hoạt, giúp React UI xử lý kéo thả cực nhanh
    private Map<String, ListConfig> lists = new HashMap<>();
    private Map<String, TaskConfig> tasks = new HashMap<>();
    private List<String> listOrder = new ArrayList<>();

    @Data
    public static class ListConfig {
        private String id = UUID.randomUUID().toString();
        private String title;
        private List<String> taskIds = new ArrayList<>();
    }

    @Data
    public static class TaskConfig {
        private String id = UUID.randomUUID().toString();
        private String title;
        private String description;
        private String coverImage;
        
        // Mảng chứa các Comment linh hoạt cho từng Task (Yêu cầu của bạn)
        private List<Comment> comments = new ArrayList<>(); 
    }

    @Data
    public static class Comment {
        private String id = UUID.randomUUID().toString();
        private String userId;   // Lưu ID của User từ Postgres
        private String userName; // Lưu kèm tên để hiển thị nhanh trên UI
        private String content;
        private String createdAt = LocalDateTime.now().toString();
    }
}