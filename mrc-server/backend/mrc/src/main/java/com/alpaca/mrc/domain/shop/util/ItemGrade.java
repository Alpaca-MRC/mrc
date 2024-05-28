package com.alpaca.mrc.domain.shop.util;

import lombok.Getter;

@Getter
public enum ItemGrade {
    COMMON(0),
    RARE(1),
    UNIQUE(2),
    LEGENDARY(3);

    private final int gradeNum;

    ItemGrade(int gradeNum) {
        this.gradeNum = gradeNum;
    }

    public static ItemGrade fromGradeNum(int gradeNum) {
        for (ItemGrade item : ItemGrade.values()) {
            if (item.getGradeNum() == gradeNum) {
                return item;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 grade 번호: " + gradeNum);
    }
}
