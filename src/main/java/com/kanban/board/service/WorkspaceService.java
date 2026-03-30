package com.kanban.board.service;

import com.kanban.board.model.User;
import com.kanban.board.model.Workspace;
import com.kanban.board.repository.UserRepository; // Thêm dòng này
import com.kanban.board.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository; // Nhúng UserRepository vào

    public Workspace createWorkspace(String name, String description, String ownerEmail) {
        // Tìm User trong hệ thống dựa vào email lấy từ Token
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người dùng!"));

        Workspace workspace = new Workspace();
        workspace.setName(name);
        workspace.setDescription(description);
        workspace.setOwnerId(owner.getId()); // Tự động lấy ID gán vào
        
        return workspaceRepository.save(workspace);
    }
}