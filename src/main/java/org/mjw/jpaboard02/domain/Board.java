package org.mjw.jpaboard02.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblboard")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 3000)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno")
    private Member member;

    //@Column(nullable = false)
    //private String author;

    @ColumnDefault(value = "0")
    private int readcount;

    public void updateReadcount() {
        readcount = readcount+1;
    }
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
}