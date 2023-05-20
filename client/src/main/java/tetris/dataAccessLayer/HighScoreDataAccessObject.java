package tetris.dataAccessLayer;

import org.apache.logging.log4j.Logger;
import tetris.logger.MyLoggerFactory;
import tetris.model.HighScore;
import tetris.model.Player;

import java.sql.*;
import java.util.ArrayList;

public class HighScoreDataAccessObject {

    private static final Logger LOGGER = MyLoggerFactory.getLogger(HighScoreDataAccessObject.class);
    public static HighScore getHighScoreDataFromDB() throws SQLException, ClassNotFoundException {
        HighScore highScore = new HighScore(new ArrayList<>());

        Class.forName("org.postgresql.Driver");

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydatabase", "postgres", "Mama204512");

        try {
            String sqlRequest = "SELECT * FROM high_scores ORDER BY score DESC";
            PreparedStatement statement = connection.prepareStatement(sqlRequest);

            try {
                ResultSet resultSet = statement.executeQuery();

                try {
                    while (resultSet.next()){
                        String username = resultSet.getString("username").strip();
                        int score = resultSet.getInt("score");
                        int level = resultSet.getInt("level");
                        int lines = resultSet.getInt("lines");
                        highScore.addScoreEntry(username, score, level, lines);
                    }
                }finally {
                    resultSet.close();
                }
            }finally {
                statement.close();
            }
        }finally {
            connection.close();
        }
        return highScore;
    }

    public static void addHighScoreDataToDB(Player player){
        try {
            Connection connection = PoolConnectionManager.getConnection();
            String sqlRequest = """
                    INSERT INTO high_scores(username, score, level, lines)
                    VALUES('%s', %d, %d, %d);
                    """.formatted(player.getName(), player.getMaxScore(), player.getMaxLevel(), player.getMaxLines());
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.executeUpdate();
        }catch (SQLException e){
            LOGGER.error("addHighScoreDataToDB error", e);
        }
    }
}
