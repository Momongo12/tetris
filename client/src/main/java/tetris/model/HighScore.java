package tetris.model;

import java.util.List;

public record HighScore(List<ScoreEntry> scoreList) {
    public void addScoreEntry(String user, int score, int level, int lines) {
        scoreList.add(new ScoreEntry(user, score, level, lines));
    }
}