package com.alpaca.mrc.domain.shop.repository;

import com.alpaca.mrc.domain.shop.entity.Cart;
import com.alpaca.mrc.domain.shop.util.ItemGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByName(String name);
    List<Cart> findByGrade(ItemGrade itemGrade);

    boolean existsByName(String name);
}
