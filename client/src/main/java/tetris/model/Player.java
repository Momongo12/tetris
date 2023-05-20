package tetris.model;

import java.util.Date;
import java.util.LinkedHashMap;

public class Player {
    private final int playerIdInDB;
    private final String name;
    private Date dateOfRegistation;
    private int numberOfGames;
    private int maxScore;
    private int averageScore;
    private int maxLines;
    private int maxLevel;

    public Player(String name, int playerIdInDB){
        this.playerIdInDB = playerIdInDB;
        this.name = name;
    }

    public LinkedHashMap<String, Integer> getPlayerStatisticMap(){
        LinkedHashMap<String, Integer> map =  new LinkedHashMap<>();
        {
            map.put("Number of Games", numberOfGames);
            map.put("Max score", maxScore);
            map.put("averageScore", averageScore);
            map.put("maxLines", maxLines);
            map.put("maxLevel", maxLevel);
        }
        return map;
    }

    public void updateStatisticPlayer(int currentScore, int currentLines, int currentLevel){
        maxScore = Math.max(currentScore, maxScore);
        maxLevel = Math.max(currentLevel, maxLevel);
        maxLines = Math.max(currentLines, maxLines);
        numberOfGames++;
        averageScore = (averageScore + currentScore) / numberOfGames;
    }

    public String getName(){ return name; }

    public Date getDateOfRegistation() {
        return dateOfRegistation;
    }

    public void setDateOfRegistation(Date dateOfRegistation) {
        this.dateOfRegistation = dateOfRegistation;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public void setAverageScore(int averageScore) { this.averageScore = averageScore; }

    public int getAverageScore() { return averageScore; }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getPlayerIdInDB() { return playerIdInDB; }
}
