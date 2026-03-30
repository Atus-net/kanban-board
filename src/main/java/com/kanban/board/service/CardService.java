package com.kanban.board.service;

import com.kanban.board.model.Card;
import com.kanban.board.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public Card createCard(String boardId, String listId, String title, String description) {
        Card card = new Card();
        card.setBoardId(boardId);
        card.setListId(listId);
        card.setTitle(title);
        card.setDescription(description);
        
        // Thực tế phần 'order' sẽ cần query DB để lấy vị trí lớn nhất rồi +1. 
        // Nhưng tạm thời để 1000 mặc định cho thẻ đầu tiên.
        
        return cardRepository.save(card);
    }
}