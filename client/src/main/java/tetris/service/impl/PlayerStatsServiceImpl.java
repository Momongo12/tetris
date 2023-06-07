package tetris.service.impl;

import lombok.extern.log4j.Log4j2;
import tetris.model.Player;
import tetris.service.PlayerStatsService;

import java.io.IOException;
import java.net.HttpURLConnection;

import static tetris.util.ServiceUtil.*;

@Log4j2
public class PlayerStatsServiceImpl implements PlayerStatsService {
    private static final String PLAYER_STATISTIC_SERVER_BASE_URL = "http://localhost:8081/api/statistics/";

    public Player getPlayerByEmail(String email) {
        HttpURLConnection connection = null;
        try {
            String url = PLAYER_STATISTIC_SERVER_BASE_URL + "player/" + email;

            connection = createConnection(url, "GET");


            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return Player.deserialize(readResponse(connection.getInputStream()));
            } else {
                log.error("Get player Statistics failed\nstatus:" + responseCode);
            }

        } catch (IOException e) {
            log.error("Get player Statistics error", e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
