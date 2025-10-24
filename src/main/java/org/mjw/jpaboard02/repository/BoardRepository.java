package org.mjw.jpaboard02.repository;

import org.mjw.jpaboard02.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
}
