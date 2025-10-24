package org.mjw.jpaboard02.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(exclude = {"board","member"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {@Index(name="idx_reply_bno", columnList = "bno")})
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;
    //private String author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private Board board;
}
