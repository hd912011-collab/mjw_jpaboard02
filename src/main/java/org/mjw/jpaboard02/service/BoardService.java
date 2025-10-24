package org.mjw.jpaboard02.service;

import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.dto.BoardDTO;
import org.mjw.jpaboard02.dto.BoardListReplyCountDTO;
import org.mjw.jpaboard02.dto.PageRequestDTO;
import org.mjw.jpaboard02.dto.PageResponseDTO;

import java.util.List;


    public interface BoardService {
        Long insertBoard(BoardDTO boardDTO);
        List<BoardDTO> findAllBoards();
        BoardDTO findBoardById(Long bno, Integer mode);
        void updateBoard(BoardDTO boardDTO);
        void deleteBoard(Long bno);
        PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);
        PageResponseDTO<BoardListReplyCountDTO> getListReplyCount(PageRequestDTO pageRequestDTO);

        default Board dtoToEntity(BoardDTO boardDTO)
        {
            Board board = Board.builder()
                    .bno(boardDTO.getBno())
                    .title(boardDTO.getTitle())
                    .content(boardDTO.getContent())
                    .build();
            return board;
        }
        default BoardDTO entityToDTO(Board board)
        {
            BoardDTO boardDTO = BoardDTO.builder()
                    .bno(board.getBno())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .readcount(board.getReadcount())
                    .regDate(board.getRegDate())
                    .updateDate(board.getUpdateDate())
                    .build();
            return boardDTO;
        }
    }
