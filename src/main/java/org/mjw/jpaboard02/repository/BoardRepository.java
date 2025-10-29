package org.mjw.jpaboard02.repository;

import org.mjw.jpaboard02.domain.Board;
import org.mjw.jpaboard02.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardSearch {
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno=:bno")
    Optional<Board> findByIdWithImages(long bno);
}
