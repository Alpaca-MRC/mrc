package com.alpaca.mrc.domain.shop.repository;

import com.alpaca.mrc.domain.member.entity.Member;
import com.alpaca.mrc.domain.shop.entity.Cart;
import com.alpaca.mrc.domain.shop.entity.MyCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyCartRepository extends JpaRepository<MyCart, Long> {
    Optional<MyCart> findByMemberAndCart(Member member, Cart cart);

    boolean existsByMemberAndCart(Member member, Cart cart);
}
