package com.kanban.board.controller;

import com.kanban.board.model.Card;
import com.kanban.board.service.CardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CardRequest request) {
        try {
            Card card = cardService.createCard(
                    request.getBoardId(), 
                    request.getListId(), 
                    request.getTitle(),
                    request.getDescription()
            );
            return ResponseEntity.ok(card);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi tạo Card: " + e.getMessage());
        }
    }

    @Data
    static class CardRequest {
        private String boardId;
        private String listId;
        private String title;
        private String description;
    }
}