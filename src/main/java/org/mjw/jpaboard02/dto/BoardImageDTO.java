package org.mjw.jpaboard02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardImageDTO {
    private String uuid;
    private String filename;
    private int ord;
    private boolean image;
}
