package com.kanban.board.controller;

import com.kanban.board.model.Board;
import com.kanban.board.service.BoardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request) {
        try {
            Board board = boardService.createBoard(request.getWorkspaceId(), request.getTitle());
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo Board: " + e.getMessage());
        }
    }
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardDetails(@PathVariable String boardId) {
        try {
            return ResponseEntity.ok(boardService.getBoardDetails(boardId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching data: " + e.getMessage());
        }
    }

    @Data
    static class BoardRequest {
        private String workspaceId;
        private String title;
    }
}
