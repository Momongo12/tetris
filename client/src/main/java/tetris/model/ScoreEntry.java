package tetris.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

@Data
public class ScoreEntry {
    private String username;
    private int score;
    private int level;
    private int lines;

    public ScoreEntry(String username, int score, int level, int lines) {
        this.username = username;
        this.score = score;
        this.level = level;
        this.lines = lines;
    }

    public ScoreEntry() {

    }

    public static List<ScoreEntry> deserialize(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<List<ScoreEntry>>() {});
    }
}