package org.mjw.jpaboard02.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString(exclude = {"member", "imageSet"})
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

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet=new HashSet<>();
    public void addImage(String uuid, String filename){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .filename(filename)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }
    public void removeImage(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }

    public void updateReadcount() {
        readcount = readcount+1;
    }
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
}