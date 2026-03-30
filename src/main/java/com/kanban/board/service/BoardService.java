package com.kanban.board.service;

import com.kanban.board.model.Board;
import com.kanban.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String workspaceId, String title) {
        Board board = new Board();
        board.setWorkspaceId(workspaceId);
        board.setTitle(title);
        
        // Khởi tạo 3 cột mặc định chuẩn Kanban
        Board.ListConfig todoList = new Board.ListConfig();
        todoList.setTitle("To Do");
        todoList.setOrder(1000);

        Board.ListConfig doingList = new Board.ListConfig();
        doingList.setTitle("In Progress");
        doingList.setOrder(2000);

        Board.ListConfig doneList = new Board.ListConfig();
        doneList.setTitle("Done");
        doneList.setOrder(3000);

        // Nhúng 3 cột này vào Board
        board.getLists().add(todoList);
        board.getLists().add(doingList);
        board.getLists().add(doneList);

        // Lưu toàn bộ cục dữ liệu này xuống MongoDB chỉ với 1 lệnh
        return boardRepository.save(board);
    }
}