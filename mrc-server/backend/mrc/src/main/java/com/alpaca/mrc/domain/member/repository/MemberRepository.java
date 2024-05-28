package com.alpaca.mrc.domain.member.repository;

import com.alpaca.mrc.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = MemberRepository.class)
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Boolean existsByUsername(String username);
}
