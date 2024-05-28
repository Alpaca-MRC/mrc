package com.alpaca.mrc.domain.shop.entity;

import com.alpaca.mrc.domain.shop.util.ItemGrade;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cart_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_grade")
    private ItemGrade grade;
}
