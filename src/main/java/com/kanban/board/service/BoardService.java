package com.kanban.board.service;

import com.kanban.board.model.Board;
import com.kanban.board.model.Card;
import com.kanban.board.repository.BoardRepository;
import com.kanban.board.repository.CardRepository; // Import thêm CardRepository
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CardRepository cardRepository; // Nhúng CardRepository vào đây

    @Transactional
    public Board createBoard(String workspaceId, String title) {
        Board board = new Board();
        board.setWorkspaceId(workspaceId);
        board.setTitle(title);
        
        Board.ListConfig todoList = new Board.ListConfig();
        todoList.setTitle("To Do");
        todoList.setOrder(1000);

        Board.ListConfig doingList = new Board.ListConfig();
        doingList.setTitle("In Progress");
        doingList.setOrder(2000);

        Board.ListConfig doneList = new Board.ListConfig();
        doneList.setTitle("Done");
        doneList.setOrder(3000);

        board.getLists().add(todoList);
        board.getLists().add(doingList);
        board.getLists().add(doneList);

        return boardRepository.save(board);
    }

    // --- HÀM MỚI: GOM TOÀN BỘ DỮ LIỆU ---
    public BoardDetailsResponse getBoardDetails(String boardId) {
        // 1. Tìm Board
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Bảng!"));

        // 2. Tìm tất cả các thẻ (Card) thuộc về Board này
        List<Card> allCards = cardRepository.findByBoardId(boardId);

        // 3. Nhào nặn dữ liệu: Nhét Card vào đúng Cột của nó
        List<BoardDetailsResponse.ListWithCards> listsWithCards = board.getLists().stream().map(listConfig -> {
            BoardDetailsResponse.ListWithCards listDto = new BoardDetailsResponse.ListWithCards();
            listDto.setId(listConfig.getId());
            listDto.setTitle(listConfig.getTitle());
            listDto.setOrder(listConfig.getOrder());
            
            // Lọc ra thẻ thuộc Cột này và sắp xếp theo order
            List<Card> cardsInList = allCards.stream()
                    .filter(card -> card.getListId().equals(listConfig.getId()))
                    .sorted(Comparator.comparing(Card::getOrder))
                    .collect(Collectors.toList());
            
            listDto.setCards(cardsInList);
            return listDto;
        }).collect(Collectors.toList());

        // 4. Đóng gói trả về
        BoardDetailsResponse response = new BoardDetailsResponse();
        response.setId(board.getId());
        response.setTitle(board.getTitle());
        response.setWorkspaceId(board.getWorkspaceId());
        response.setLists(listsWithCards);

        return response;
    }

    // DTO (Data Transfer Object) để định hình cục JSON trả về cho Frontend
    @Data
    public static class BoardDetailsResponse {
        private String id;
        private String title;
        private String workspaceId;
        private List<ListWithCards> lists;

        @Data
        public static class ListWithCards {
            private String id;
            private String title;
            private Integer order;
            private List<Card> cards; // Danh sách thẻ nằm gọn trong cột
        }
    }
}