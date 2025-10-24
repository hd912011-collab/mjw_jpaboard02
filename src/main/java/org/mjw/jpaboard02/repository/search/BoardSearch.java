package org.mjw.jpaboard02.repository.search;

import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
    Page<BoardListReplyCountDTO> searchWithReplycount(String[] types, String keyword, Pageable pageable);
}
