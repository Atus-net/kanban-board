package com.kanban.board.service;

import com.kanban.board.model.Workspace;
import com.kanban.board.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public Workspace createWorkspace(String name, String description, UUID ownerId) {
        Workspace workspace = new Workspace();
        workspace.setName(name);
        workspace.setDescription(description);
        workspace.setOwnerId(ownerId); // Gán người tạo
        
        return workspaceRepository.save(workspace);
    }
}