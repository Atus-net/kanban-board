package com.kanban.board.repository;

import com.kanban.board.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {
    
    // Tìm tất cả Card trong một cột và sắp xếp theo thứ tự (order) tăng dần
    List<Card> findByListIdOrderByOrderAsc(String listId);
    
    // Tìm tất cả Card của một Board (dành cho tính năng tìm kiếm tổng)
    List<Card> findByBoardId(String boardId);
}