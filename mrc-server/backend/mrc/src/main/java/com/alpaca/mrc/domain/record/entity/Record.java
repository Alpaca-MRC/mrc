package com.alpaca.mrc.domain.record.entity;

import com.alpaca.mrc.domain.game.util.GameMode;
import com.alpaca.mrc.domain.game.util.GamePlayerNum;
import com.alpaca.mrc.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "lap_time")
    private LocalDateTime lapTime;

    @Column(name = "distance")
    private int distance;

    @Column(name = "avg_speed")
    private int avgSpeed;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_mode")
    private GameMode gameMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_player_num")
    private GamePlayerNum gamePlayerNum;

    @Column(name = "is_win")
    private boolean isWin;

    @Column(name = "map_url")
    private String mapUrl;

    @Column(name = "replay_url")
    private String replayUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
