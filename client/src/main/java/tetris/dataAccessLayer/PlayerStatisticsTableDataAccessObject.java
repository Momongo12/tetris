package tetris.dataAccessLayer;

import org.apache.logging.log4j.Logger;
import tetris.logger.MyLoggerFactory;
import tetris.model.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PlayerStatisticsTableDataAccessObject {

    private static final Logger LOGGER = MyLoggerFactory.getLogger(PlayerStatisticsTableDataAccessObject.class);
    public static void initStatisticsForUser(String username) {
        try {
            Connection connection = PoolConnectionManager.getConnection();
            String sqlRequest = """
                    INSERT INTO user_statistics(user_id, max_score, max_lines, max_level, date_of_registration, number_of_games)
                    VALUES((SELECT id FROM users WHERE username='%s'), 0, 0, 0, CURRENT_DATE, 0);
                    """.formatted(username);
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("error initializing statistics for a user", e);
        }
    }

    public static Player getPlayerStatistics(String username) {
        Player user = null;
        try {
            Connection connection = PoolConnectionManager.getConnection();
            String sqlRequest = """
                    SELECT user_id, max_score, max_lines, max_level, date_of_registration, number_of_games, average_score
                    FROM
                        user_statistics INNER JOIN users
                        on user_statistics.user_id = users.id
                    WHERE username='%s';
                    """.formatted(username);
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            user = new Player(username, resultSet.getInt("user_id"));
            user.setMaxScore(resultSet.getInt("max_score"));
            user.setAverageScore(resultSet.getInt("average_score"));
            user.setMaxLines(resultSet.getInt("max_lines"));
            user.setMaxLevel(resultSet.getInt("max_level"));
            user.setNumberOfGames(resultSet.getInt("number_of_games"));
            user.setDateOfRegistation(resultSet.getDate("date_of_registration"));
        } catch (SQLException e) {
            LOGGER.error("error getting player statistics for pl", e);
        }
        return user;
    }

    public static void updatePlayerStatisticsInDB(Player player) {
        if (Objects.equals(player.getName(), "Unknown")) return;

        try {
            Connection connection = PoolConnectionManager.getConnection();
            String sqlRequest = """
                    UPDATE user_statistics
                    SET max_score = %d, max_lines = %d, max_level = %d, number_of_games = %d, average_score = %d
                    WHERE user_id=%d;
                    """.formatted(player.getMaxScore(), player.getMaxLines(), player.getMaxLevel(), player.getNumberOfGames(),
                                    player.getAverageScore(), player.getPlayerIdInDB());
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error update player statistics in DB", e);
        }
    }
}
