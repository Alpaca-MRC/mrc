package com.alpaca.mrc.domain.profile.dto.response;

import com.alpaca.mrc.domain.shop.util.ItemGrade;
import lombok.Builder;

@Builder
public record CartDTO(String name, ItemGrade grade) {

    public static CartDTO of(String name, ItemGrade grade) {
        return CartDTO.builder()
                .name(name)
                .grade(grade)
                .build();
    }
}
