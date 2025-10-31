package org.mjw.jpaboard02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.domain.Member;
import org.mjw.jpaboard02.dto.*;
import org.mjw.jpaboard02.repository.BoardRepository;
import org.mjw.jpaboard02.repository.MemberRepository;
import org.mjw.jpaboard02.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
//@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public Long insertBoard(BoardDTO boardDTO){
        Board board = dtoToEntity(boardDTO);
        Member member=memberRepository.findByUsername(boardDTO.getAuthor());
        board.setMember(member);
        Long bno=boardRepository.save(board).getBno();//save는 저장한 Entity를 리턴
        return bno;
    }

    @Override
    public List<BoardDTO> findAllBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDTO> dtos = new ArrayList<>();
        for(Board board : boards){
           dtos.add(entityToDTO(board));
        }
        return dtos;
    }

    @Override
    public BoardDTO findBoardById(Long bno, Integer mode) {
        //Board board = boardRepository.findById(bno).orElse(null); //Optional<Board>
        Board board = boardRepository.findByIdWithImages(bno).orElse(null);
        if(mode==1){
            board.updateReadcount(); //Optional<Board>
            boardRepository.save(board);
        }
        BoardDTO dto = entityToDTO(board);
        dto.setAuthor(board.getMember().getUsername());
        return dto;
    }

    @Override
    public void updateBoard(BoardDTO boardDTO) {
        Board board = boardRepository.findById(boardDTO.getBno()).orElse(null);
        board.change(boardDTO.getTitle(), boardDTO.getContent());
        if(boardDTO.getBoardImageDTOS()!=null){
            board.removeImage();
            for(BoardImageDTO imgDTO : boardDTO.getBoardImageDTOS()){
                board.addImage(imgDTO.getUuid(), imgDTO.getFilename(), imgDTO.isImage());
            }
        }
        boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Long bno) {
        Board board = boardRepository.findByIdWithImages(bno).orElse(null);
        board.removeImage();
        //replyRepository.deleteByBoardId(bno);
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("bno");
        // Page<Board> result = boardRepository.findAll(pageable);
        // Page<Board> result = boardRepository.findKeyword(pageRequestDTO.getKeyword(),pageable);
        Page<Board> result = boardRepository.searchAll(
                pageRequestDTO.getTypes(),
                pageRequestDTO.getKeyword(),
                pageable);
        List<BoardDTO> dtoList = result.getContent().stream()
                .map( board -> entityToDTO(board))
                .collect(Collectors.toList());
        int total = (int)result.getTotalElements();

        PageResponseDTO<BoardDTO> responseDTO = PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();

        return responseDTO;
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> getListReplyCount(PageRequestDTO pageRequestDTO) {
        String [] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplycount(types, keyword, pageable);
        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

}
