package com.kanban.board.service;

import com.kanban.board.model.Board;
import com.kanban.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String workspaceId, String title, String coverImage) {
        Board board = new Board();
        board.setWorkspaceId(workspaceId);
        board.setTitle(title);
        board.setCoverImage(coverImage);

        // 1. Khởi tạo 3 cột mặc định bằng cấu trúc Map mới
        Board.ListConfig todo = new Board.ListConfig();
        todo.setTitle("To Do");
        
        Board.ListConfig inProgress = new Board.ListConfig();
        inProgress.setTitle("In Progress");
        
        Board.ListConfig done = new Board.ListConfig();
        done.setTitle("Done");

        // 2. Đẩy vào Map 'lists'
        board.getLists().put(todo.getId(), todo);
        board.getLists().put(inProgress.getId(), inProgress);
        board.getLists().put(done.getId(), done);

        // 3. Thiết lập thứ tự xuất hiện của các cột
        board.getListOrder().add(todo.getId());
        board.getListOrder().add(inProgress.getId());
        board.getListOrder().add(done.getId());

        return boardRepository.save(board);
    }

    public Board getBoardDetails(String boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Board!"));
    }
    
    // Hàm tạo Task mới
    public Board.TaskConfig addTask(String boardId, String listId, String title) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Không tìm thấy Board!"));
        
        // Tạo Task mới
        Board.TaskConfig newTask = new Board.TaskConfig();
        newTask.setTitle(title);
        
        // Đẩy vào Map tasks và cập nhật taskIds của List tương ứng
        board.getTasks().put(newTask.getId(), newTask);
        board.getLists().get(listId).getTaskIds().add(newTask.getId());
        
        boardRepository.save(board);
        return newTask;
    }

    // Hàm thêm Comment (BẠN ĐANG THIẾU HÀM NÀY)
    public Board.Comment addComment(String boardId, String taskId, String userId, String userName, String content) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Board"));

        // Tìm Task đang cần comment
        Board.TaskConfig task = board.getTasks().get(taskId);
        if (task == null) {
            throw new RuntimeException("Không tìm thấy Task trong Board này");
        }

        // Tạo Comment
        Board.Comment newComment = new Board.Comment();
        newComment.setUserId(userId);
        newComment.setUserName(userName);
        newComment.setContent(content);

        // Nhúng Comment vào Task
        task.getComments().add(newComment);
        boardRepository.save(board); 
        
        return newComment;
    }

    public Board.TaskConfig updateTask(String boardId, String taskId, String title, String description, String coverImage) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Board"));
        
        Board.TaskConfig task = board.getTasks().get(taskId);
        if (task == null) {
            throw new RuntimeException("Không tìm thấy Task");
        }


        if (title != null) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (coverImage != null) task.setCoverImage(coverImage);

        boardRepository.save(board);
        return task;
    }


    public Board moveTask(String boardId, String sourceListId, String destinationListId, 
                          java.util.List<String> sourceTaskIds, java.util.List<String> destinationTaskIds) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Board"));

        // Cập nhật lại thứ tự Task cho cột bị lấy mất Task (Source)
        Board.ListConfig sourceList = board.getLists().get(sourceListId);
        if (sourceList != null) {
            sourceList.setTaskIds(sourceTaskIds);
        }


        if (!sourceListId.equals(destinationListId)) {
            Board.ListConfig destList = board.getLists().get(destinationListId);
            if (destList != null) {
                destList.setTaskIds(destinationTaskIds);
            }
        }

        return boardRepository.save(board);
    }
    // ==========================================
    // CÁC API XÓA (DELETE) - 10% CUỐI CÙNG
    // ==========================================

    // 1. Xóa Bình luận (Comment)
    public void deleteComment(String boardId, String taskId, String commentId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Board"));
        
        Board.TaskConfig task = board.getTasks().get(taskId);
        if (task != null) {
            // Duyệt qua mảng comments và xóa comment có ID trùng khớp
            task.getComments().removeIf(comment -> comment.getId().equals(commentId));
            boardRepository.save(board);
        }
    }

    // 2. Xóa Thẻ công việc (Task)
    public void deleteTask(String boardId, String listId, String taskId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Board"));
        
        // Bước 1: Rút ID của Task này ra khỏi mảng taskIds của List
        Board.ListConfig list = board.getLists().get(listId);
        if (list != null) {
            list.getTaskIds().remove(taskId);
        }
        
        // Bước 2: Xóa sổ hoàn toàn dữ liệu của Task này khỏi Map tasks
        board.getTasks().remove(taskId);
        
        boardRepository.save(board);
    }

    // 3. Xóa Cột (List) và dọn rác
    public void deleteList(String boardId, String listId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Board"));
        
        Board.ListConfig list = board.getLists().get(listId);
        if (list != null) {
            // Dọn rác: Xóa toàn bộ các Task đang nằm trong Cột này cho sạch Database
            list.getTaskIds().forEach(taskId -> board.getTasks().remove(taskId));
        }
        
        // Gỡ Cột này khỏi Map lists và mảng listOrder
        board.getLists().remove(listId);
        board.getListOrder().remove(listId);
        
        boardRepository.save(board);
    }
}