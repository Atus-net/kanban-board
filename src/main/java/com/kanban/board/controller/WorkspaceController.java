package com.kanban.board.controller;

import com.kanban.board.model.Workspace;
import com.kanban.board.service.WorkspaceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<?> createWorkspace(@RequestBody WorkspaceRequest request) {
        try {
            // Tạm thời chúng ta truyền ownerId từ request body lên. 
            // (Sau khi tích hợp Filter chặn JWT, chúng ta sẽ lấy ID tự động từ Token)
            Workspace workspace = workspaceService.createWorkspace(
                    request.getName(), 
                    request.getDescription(), 
                    request.getOwnerId()
            );
            return ResponseEntity.ok(workspace);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo Workspace: " + e.getMessage());
        }
    }

    @Data
    static class WorkspaceRequest {
        private String name;
        private String description;
        private UUID ownerId;
    }
}