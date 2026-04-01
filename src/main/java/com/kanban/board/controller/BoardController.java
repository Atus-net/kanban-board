package com.kanban.board.controller;

import com.kanban.board.model.Board;
import com.kanban.board.service.BoardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request) {
        try {
            // Chuyển đổi UUID sang String để khớp với tham số của Service
            Board board = boardService.createBoard(
                request.getOwnerId().toString(), 
                request.getTitle(), 
                request.getCoverImage()
            );
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

    @PostMapping("/{boardId}/lists/{listId}/tasks")
    public ResponseEntity<?> addTask(
            @PathVariable String boardId,
            @PathVariable String listId,
            @RequestBody TaskRequest request) {
        try {
            Board.TaskConfig task = boardService.addTask(boardId, listId, request.getTitle());
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{boardId}/tasks/{taskId}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable String boardId,
            @PathVariable String taskId,
            @RequestBody CommentRequest request) { 
        try {
            Board.Comment comment = boardService.addComment(
                    boardId, 
                    taskId, 
                    request.getUserId(), 
                    request.getUserName(), 
                    request.getContent()
            );
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{boardId}/tasks/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable String boardId,
            @PathVariable String taskId,
            @RequestBody UpdateTaskRequest request) {
        try {
            Board.TaskConfig task = boardService.updateTask(
                    boardId, taskId, request.getTitle(), request.getDescription(), request.getCoverImage()
            );
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{boardId}/move-task")
    public ResponseEntity<?> moveTask(
            @PathVariable String boardId,
            @RequestBody MoveTaskRequest request) {
        try {
            Board board = boardService.moveTask(
                    boardId, 
                    request.getSourceListId(), 
                    request.getDestinationListId(), 
                    request.getSourceTaskIds(), 
                    request.getDestinationTaskIds()
            );
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable String boardId,
            @PathVariable String taskId,
            @PathVariable String commentId) {
        try {
            boardService.deleteComment(boardId, taskId, commentId);
            return ResponseEntity.ok().body("{\"message\": \"Đã xóa bình luận thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardId}/lists/{listId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable String boardId,
            @PathVariable String listId,
            @PathVariable String taskId) {
        try {
            boardService.deleteTask(boardId, listId, taskId);
            return ResponseEntity.ok().body("{\"message\": \"Đã xóa thẻ công việc thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{boardId}/lists/{listId}")
    public ResponseEntity<?> deleteList(
            @PathVariable String boardId,
            @PathVariable String listId) {
        try {
            boardService.deleteList(boardId, listId);
            return ResponseEntity.ok().body("{\"message\": \"Đã xóa cột thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // CÁC CLASS DTO HỨNG DỮ LIỆU TỪ REQUEST BODY
    // ==========================================

    @Data
    static class BoardRequest {
        private UUID ownerId;
        private String title;
        private String coverImage;
    }

    @Data
    static class TaskRequest {
        private String title;
    }

    @Data
    static class CommentRequest {
        private String userId;
        private String userName;
        private String content;
    }
    @Data
    static class UpdateTaskRequest {
        private String title;
        private String description;
        private String coverImage;
    }

    @Data
    static class MoveTaskRequest {
        private String sourceListId;
        private String destinationListId;
        private java.util.List<String> sourceTaskIds;
        private java.util.List<String> destinationTaskIds;
    }
}