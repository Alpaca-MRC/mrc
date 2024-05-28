package com.alpaca.mrc.domain.profile.dto.response;

import com.alpaca.mrc.domain.shop.util.ItemGrade;
import lombok.Builder;

@Builder
public record AvatarDTO(String name, ItemGrade grade) {

    public static AvatarDTO of(String name, ItemGrade grade) {
        return AvatarDTO.builder()
                .name(name)
                .grade(grade)
                .build();
    }
}
