package com.alpaca.mrc.domain.shop.repository;

import com.alpaca.mrc.domain.shop.entity.Avatar;
import com.alpaca.mrc.domain.shop.util.ItemGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByName(String name);
    List<Avatar> findByGrade(ItemGrade itemGrade);

    boolean existsByName(String name);
}
