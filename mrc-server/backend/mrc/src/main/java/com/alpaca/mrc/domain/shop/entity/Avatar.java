package com.alpaca.mrc.domain.shop.entity;

import com.alpaca.mrc.domain.shop.util.ItemGrade;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "avatar")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "avatar_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "avatar_grade")
    private ItemGrade grade;


}
