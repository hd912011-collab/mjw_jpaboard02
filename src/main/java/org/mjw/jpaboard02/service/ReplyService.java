package org.mjw.jpaboard02.service;

import org.mjw.jpaboard02.domain.Reply;
import org.mjw.jpaboard02.dto.PageRequestDTO;
import org.mjw.jpaboard02.dto.PageResponseDTO;
import org.mjw.jpaboard02.dto.ReplyDTO;

import java.util.List;

public interface ReplyService {
    Long insertReply(ReplyDTO replyDTO);
    ReplyDTO findById(Long rno);
    void modifyReply(ReplyDTO replyDTO);
    void deleteReply(Long rno);
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTODTO);

    default Reply dtoToEntity(ReplyDTO replyDTO) {
        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .content(replyDTO.getContent())
                .build();
        return reply;
    }
    default ReplyDTO entityToDto(Reply reply) {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(reply.getRno())
                .content(reply.getContent())
                //.author(reply.getMember().getUsername())
                .regDate(reply.getRegDate())
                .updateDate(reply.getUpdateDate())
                .bno(reply.getBoard().getBno())
                .build();
        return replyDTO;
    }
}
