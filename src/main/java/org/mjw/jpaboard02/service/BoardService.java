package org.mjw.jpaboard02.service;

import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.domain.BoardImage;
import org.mjw.jpaboard02.dto.*;

import java.util.List;
import java.util.stream.Collectors;


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

            if(boardDTO.getBoardImageDTOS()!=null){
                boardDTO.getBoardImageDTOS().forEach(file -> {
                    //String[] arr=filename.split("_");
                    board.addImage(file.getUuid(), file.getFilename(), file.isImage());
                });
            }
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
            List<BoardImageDTO> imgBoardDTO=board.getImageSet().stream()
                    .sorted()
                    .map(img->imgEntityToDTO(img))
                    .collect(Collectors.toList());
            boardDTO.setBoardImageDTOS(imgBoardDTO);
            return boardDTO;
        }
        default BoardImageDTO imgEntityToDTO(BoardImage boardImage){
            BoardImageDTO dto=BoardImageDTO.builder()
                    .uuid(boardImage.getUuid())
                    .filename(boardImage.getFilename())
                    .image(boardImage.isImage())
                    .ord(boardImage.getOrd())
                    .build();
            return dto;
        }
    }
