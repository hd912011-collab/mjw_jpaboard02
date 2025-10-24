package org.mjw.jpaboard02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.domain.Member;
import org.mjw.jpaboard02.domain.Reply;
import org.mjw.jpaboard02.dto.PageRequestDTO;
import org.mjw.jpaboard02.dto.PageResponseDTO;
import org.mjw.jpaboard02.dto.ReplyDTO;
import org.mjw.jpaboard02.repository.BoardRepository;
import org.mjw.jpaboard02.repository.MemberRepository;
import org.mjw.jpaboard02.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long insertReply(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        Board board=boardRepository.findById(replyDTO.getBno()).orElse(null);
        Member member=memberRepository.findByUsername(replyDTO.getAuthor());
        reply.setBoard(board);
        reply.setMember(member);
        Long rno=replyRepository.save(reply).getRno();
        return rno;
    }

    @Override
    public ReplyDTO findById(Long rno) {
        Reply reply = replyRepository.findById(rno).orElse(null);
        return entityToDto(reply);
    }

    @Override
    public void modifyReply(ReplyDTO replyDTO) {
        Reply reply = replyRepository.findById(replyDTO.getRno()).orElse(null);
        reply.setContent(replyDTO.getContent());
        replyRepository.save(reply);
    }

    @Override
    public void deleteReply(Long rno) {
        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("rno");
        Page<Reply> result=replyRepository.findByBoardId(bno,pageable);
        List<ReplyDTO> dtoList=result.getContent().stream()
                .map(reply ->  entityToDto(reply))
                .collect(Collectors.toList());
        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .total((int)result.getTotalElements())
                .dtoList(dtoList)
                .build();
    }
}
