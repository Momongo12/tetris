package tetris.model;

import java.util.ArrayList;

public record HighScore(ArrayList<ScoreEntry> scoreList) {

    public void addScoreEntry(String user, int score, int level, int lines) {
        scoreList.add(new ScoreEntry(user, score, level, lines));
    }

    public record ScoreEntry(String user, int score, int level, int lines) {}
}