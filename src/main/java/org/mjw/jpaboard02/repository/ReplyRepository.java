package org.mjw.jpaboard02.repository;

import org.mjw.jpaboard02.domain.Reply;
import org.mjw.jpaboard02.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply,Long>, BoardSearch {
}
