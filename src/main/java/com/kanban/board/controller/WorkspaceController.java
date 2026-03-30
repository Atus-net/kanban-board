package com.kanban.board.controller;

import com.kanban.board.model.Workspace;
import com.kanban.board.service.WorkspaceService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // Import Principal

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    // Thêm tham số Principal (Đại diện cho User đang đăng nhập hợp lệ)
    public ResponseEntity<?> createWorkspace(@RequestBody WorkspaceRequest request, Principal principal) {
        try {
            // principal.getName() sẽ trả về email mà chúng ta đã giải mã từ JWT Token
            String ownerEmail = principal.getName(); 

            Workspace workspace = workspaceService.createWorkspace(
                    request.getName(), 
                    request.getDescription(), 
                    ownerEmail
            );
            return ResponseEntity.ok(workspace);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo Workspace: " + e.getMessage());
        }
    }

    // Body gửi lên giờ chỉ cần Name và Description, KHÔNG CẦN ownerId nữa
    @Data
    static class WorkspaceRequest {
        private String name;
        private String description;
    }
}