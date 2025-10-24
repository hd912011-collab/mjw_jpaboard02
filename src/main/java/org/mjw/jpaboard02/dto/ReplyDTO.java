package org.mjw.jpaboard02.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {
    private Long rno;
    @NotEmpty
    private Long bno;
    @NotEmpty
    private String author;
    @NotEmpty
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
