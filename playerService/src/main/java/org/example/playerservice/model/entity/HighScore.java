package org.example.playerservice.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("high_scores")
public class HighScore {
    @Id
    @Column("high_score_id")
    private Long highScoreId;

    @Column("max_score")
    private int maxScore;

    @Column("max_level")
    private int maxLevel;

    @Column("max_lines")
    private int maxLines;

    @MappedCollection(idColumn = "player_id", keyColumn = "player_id")
    private Player player;

    public HighScore () {

    }
}