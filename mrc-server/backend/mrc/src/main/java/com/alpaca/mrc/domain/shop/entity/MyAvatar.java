package com.alpaca.mrc.domain.shop.entity;

import com.alpaca.mrc.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "my_avatar")
public class MyAvatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @Column(name = "is_selected")
    private boolean isSelected;

    public void updateIsSelected() {
        this.isSelected = !this.isSelected;
    }

    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            member.getMyAvatars().add(this);
        }
    }

}
