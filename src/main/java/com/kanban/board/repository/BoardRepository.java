package com.kanban.board.repository;

import com.kanban.board.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
    // Tìm các bảng thuộc về một Workspace (Postgres ID)
    List<Board> findByWorkspaceId(String workspaceId);
}