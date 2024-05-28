package com.alpaca.mrc.domain.member.entity;

import com.alpaca.mrc.domain.record.entity.Record;
import com.alpaca.mrc.domain.shop.entity.MyAvatar;
import com.alpaca.mrc.domain.shop.entity.MyCart;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Record> records;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MyCart> myCarts;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MyAvatar> myAvatars;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "coin", columnDefinition = "int default 0")
    private int coin;

    @Column(name = "selected_cart_name")
    private String selectedCartName;

    @Column(name = "selected_avatar_name")
    private String selectedAvatarName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSelectedCartName(String name) {
        this.selectedCartName = name;
    }

    public void updateSelectedAvatarName(String name) {
        this.selectedAvatarName = name;
    }
}
