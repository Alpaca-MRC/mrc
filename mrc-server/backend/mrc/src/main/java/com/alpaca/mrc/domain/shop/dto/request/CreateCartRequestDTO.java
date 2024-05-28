package com.alpaca.mrc.domain.shop.dto.request;

import com.alpaca.mrc.domain.shop.util.ItemGrade;
import lombok.Getter;

@Getter
public class CreateCartRequestDTO {

    String name;
    ItemGrade grade;
}
