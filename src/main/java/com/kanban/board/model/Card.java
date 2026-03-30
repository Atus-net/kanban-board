package com.kanban.board.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "cards")
public class Card {
    
    @Id
    private String id;

    // 2 Tọa độ cực kỳ quan trọng để truy vấn
    private String boardId; // Nằm trong Bảng nào?
    private String listId;  // Đứng ở Cột nào?
    
    private String title;
    private String description;
    
    // Dùng để sắp xếp thứ tự khi người dùng kéo thả thẻ (Drag & Drop)
    private Integer order = 1000; 
    
    private LocalDateTime createdAt = LocalDateTime.now();
}