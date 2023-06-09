package tetris.service.impl;

import lombok.extern.log4j.Log4j2;
import tetris.model.Player;
import tetris.service.PlayerStatsService;

import java.io.IOException;
import java.net.HttpURLConnection;

import static tetris.util.ServiceUtil.*;

/**
 * @author denMoskvin
 * @version 1.0
 */
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
                log.info("get player statistic successful");
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

    public void updateStatisticPlayer(Player player) {
        HttpURLConnection connection = null;
        try {
            String url = PLAYER_STATISTIC_SERVER_BASE_URL + "player/" + player.getPlayerIdInDB();

            connection = createConnection(url, "POST");

            sendRequest(connection, player);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.info("Player statistic updated");
            } else {
                log.error("Player status update failed\nstatus:" + responseCode);
            }

        } catch (IOException e) {
            log.error("Player status update error", e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
