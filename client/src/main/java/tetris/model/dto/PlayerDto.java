package tetris.model.dto;

import lombok.Data;

@Data
public class PlayerDto {
    private int playerIdInDB;
    private int maxScore;
    private int averageScore;
    private int maxLines;
    private int maxLevel;
}
