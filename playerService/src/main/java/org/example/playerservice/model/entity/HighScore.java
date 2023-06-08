package org.example.playerservice.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("high_scores")
public class HighScore {
    @Id
    @Column("high_score_id")
    private Long highScoreId;

    @Column("player_id")
    private Long playerId;

    @Column("max_score")
    private int maxScore;

    @Column("max_level")
    private int maxLevel;

    @Column("max_lines")
    private int maxLines;

    public HighScore () {

    }

    public HighScore(int maxScore, int maxLevel, int maxLines, Long playerId) {
        this.maxScore = maxScore;
        this.maxLevel = maxLevel;
        this.maxLines = maxLines;
        this.playerId = playerId;
    }
}