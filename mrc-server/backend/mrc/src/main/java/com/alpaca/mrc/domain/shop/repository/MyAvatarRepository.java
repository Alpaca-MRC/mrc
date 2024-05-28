package com.alpaca.mrc.domain.shop.repository;

import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.shop.entity.Avatar;
import com.alpaca.mrc.domain.shop.entity.MyAvatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyAvatarRepository extends JpaRepository<MyAvatar, Long> {
    Optional<MyAvatar> findByMemberAndAvatar(Member member, Avatar avatar);

    boolean existsByMemberAndAvatar(Member member, Avatar avatar);
}
