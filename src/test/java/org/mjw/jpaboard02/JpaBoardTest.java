package org.mjw.jpaboard02;

import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.mjw.jpaboard02.domain.Member;
import org.mjw.jpaboard02.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class JpaBoardTest {
    @Autowired
    private MemberRepository memberRepository;
    @Test
    public void insertMember() {
        Member member = Member.builder()
                .name("홍길동")
                .password("123456")
                .username("damin")
                .build();
        memberRepository.save(member);
    }
}
