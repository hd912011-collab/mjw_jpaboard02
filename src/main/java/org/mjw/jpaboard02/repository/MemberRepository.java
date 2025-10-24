package org.mjw.jpaboard02.repository;

import org.mjw.jpaboard02.domain.Member;
import org.mjw.jpaboard02.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByUsername(String username);
}
