package org.mjw.jpaboard02.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage implements Comparable<BoardImage> {
    @Id
    private String uuid;
    private String filename;
    private int ord;
    private boolean image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bno")
    private Board board;

    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }
    public void changeBoard(Board board) {
        this.board = board;
    }


}
