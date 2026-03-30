package com.kanban.board.repository;

import com.kanban.board.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    // Chỉ cần khai báo tên hàm, Spring Boot tự động viết code tìm kiếm trong MongoDB!
    List<Board> findByWorkspaceId(String workspaceId);
}