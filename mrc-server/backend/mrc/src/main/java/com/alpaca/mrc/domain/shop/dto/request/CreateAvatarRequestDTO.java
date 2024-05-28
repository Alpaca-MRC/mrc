package com.alpaca.mrc.domain.shop.dto.request;

import com.alpaca.mrc.domain.shop.util.ItemGrade;
import lombok.Getter;

@Getter
public class CreateAvatarRequestDTO {

    String name;
    ItemGrade grade;
}
