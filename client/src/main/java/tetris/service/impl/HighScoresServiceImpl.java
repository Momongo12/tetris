package tetris.service.impl;

import lombok.extern.log4j.Log4j2;
import tetris.model.HighScore;
import tetris.model.ScoreEntry;
import tetris.service.HighScoresService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import static tetris.util.ServiceUtil.createConnection;
import static tetris.util.ServiceUtil.readResponse;

@Log4j2
public class HighScoresServiceImpl implements HighScoresService {

    private static final String HIGH_SCORES_SERVER_BASE_URL = "http://localhost:8081/api/";

    public HighScore getHighScores() {
        HttpURLConnection connection = null;
        try {
            String url = HIGH_SCORES_SERVER_BASE_URL + "high_scores";

            connection = createConnection(url, "GET");


            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.info("Get high scores successful");
                List<ScoreEntry> scoreEntries = ScoreEntry.deserialize(readResponse(connection.getInputStream()));
                scoreEntries.sort((e1, e2) -> Integer.compare(e2.getScore(), e1.getScore()));
                return new HighScore(scoreEntries);
            } else {
                log.error("Get high scores failed\nstatus:" + responseCode);
            }

        } catch (IOException e) {
            log.error("Get high scores error", e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
