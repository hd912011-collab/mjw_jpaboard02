package org.mjw.jpaboard02.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BoardListReplyCountDTO {
    private Long bno;
    private String title;
    private String author;
    private int readcount;
    private LocalDateTime regDate;
    private Long replyCount;

}
