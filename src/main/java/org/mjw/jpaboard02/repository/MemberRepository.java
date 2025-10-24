package org.mjw.jpaboard02.repository;

import org.mjw.jpaboard02.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
