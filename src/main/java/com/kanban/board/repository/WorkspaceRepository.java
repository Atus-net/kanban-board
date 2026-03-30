package com.kanban.board.repository;

import com.kanban.board.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    // Spring Data JPA đã lo sẵn các hàm save(), findById(), delete()...
}