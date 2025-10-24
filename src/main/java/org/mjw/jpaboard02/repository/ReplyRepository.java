package org.mjw.jpaboard02.repository;

import org.mjw.jpaboard02.domain.Reply;
import org.mjw.jpaboard02.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long>{
    @Query("select r from Reply r where r.board.bno=:boardId")
    Page<Reply> findByBoardId(Long bno, Pageable pageable);
}
