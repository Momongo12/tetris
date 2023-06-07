package org.example.playerservice.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("player_statistic")
public class PlayerStatistic {
    @Id
    @Column("player_statistic_id")
    private Long playerStatisticId;

    @Column("player_id")
    private Long playerId;

    @Column("max_score")
    private int maxScore;

    @Column("max_level")
    private int maxLevel;

    @Column("max_lines")
    private int maxLines;

    @Column("number_of_games")
    private int numberOfGames;

    @Column("average_score")
    private int averageScore;

    public PlayerStatistic(Long playerId) {
        this.playerId = playerId;
    }
}