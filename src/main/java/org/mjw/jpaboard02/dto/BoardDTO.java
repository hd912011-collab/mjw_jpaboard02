package org.mjw.jpaboard02.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long bno;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
    private String author;
    private int readcount;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private List<BoardImageDTO> boardImageDTOS;

}
