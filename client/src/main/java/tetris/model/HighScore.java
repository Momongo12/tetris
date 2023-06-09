package tetris.model;

import java.util.List;

/**
 * @author denMoskvin
 * @version 1.0
 */
public record HighScore(List<ScoreEntry> scoreList) {
    public void addScoreEntry(String user, int score, int level, int lines) {
        scoreList.add(new ScoreEntry(user, score, level, lines));
    }
}