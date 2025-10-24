package org.mjw.jpaboard02;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.domain.Member;
import org.mjw.jpaboard02.domain.Reply;
import org.mjw.jpaboard02.dto.BoardListReplyCountDTO;
import org.mjw.jpaboard02.repository.BoardRepository;
import org.mjw.jpaboard02.repository.MemberRepository;
import org.mjw.jpaboard02.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
@Log4j2
public class JpaBoardTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertMember() {
        Member member = Member.builder()
                .name("홍길동")
                .password("123456")
                .username("admin")
                .build();
        memberRepository.save(member);
    }

    @Test
    public void insertBoard() {
        Member member = memberRepository.findByUsername("admin2");
        Board board = Board.builder()
                .title("title2")
                .content("content2")
                .member(member)
                .build();
        boardRepository.save(board);
    }

    @Test
    public void insertReply() {
        Member member = memberRepository.findByUsername("admin");
        Board board = boardRepository.findById(1L).orElse(null);
        Reply reply = Reply.builder()
                .member(member)
                .board(board)
                .content("Hello World!")
                .build();
        replyRepository.save(reply);
    }

    @Test
    public void getReplyList() {
//        List<Reply> replies=replyRepository.findByBoardId(1L);
//        for(Reply reply:replies){
//            log.info(reply);
//            log.info(reply.getRno());
//            log.info(reply.getContent());
//            log.info(reply.getMember());
//            log.info(reply.getBoard());
    }

    @Test
    public void testSearchReplyCount() {
        String[] types = {"t", "c"};
        String keyword = "t";
        Pageable pageable = PageRequest.of(0, 3, Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplycount(types, keyword, pageable);

        List<BoardListReplyCountDTO> list = result.getContent();
        for (BoardListReplyCountDTO dto : list) {
            log.info(dto.toString());
        }
    }
}
