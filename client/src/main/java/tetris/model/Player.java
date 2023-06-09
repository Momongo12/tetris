package tetris.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author denMoskvin
 * @version 1.0
 */
@Data
public class Player {
    private int playerIdInDB;
    private String name;
    private Date dateOfRegistation;
    private int numberOfGames;
    private int maxScore;
    private int averageScore;
    private int maxLines;
    private int maxLevel;

    public Player() {

    }

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

    public void updateStatistic(int currentScore, int currentLines, int currentLevel){
        maxScore = Math.max(currentScore, maxScore);
        maxLevel = Math.max(currentLevel, maxLevel);
        maxLines = Math.max(currentLines, maxLines);
        numberOfGames++;
        averageScore = (averageScore + currentScore) / numberOfGames;
    }

    public String serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static Player deserialize(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Player.class);
    }
}
