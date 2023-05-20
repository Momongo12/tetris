package tetris.dataAccessLayer;


import org.apache.logging.log4j.Logger;
import tetris.logger.MyLoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerTableDataAccessObject {
    private static final Logger LOGGER = MyLoggerFactory.getLogger(PlayerTableDataAccessObject.class);
    public static boolean doesUserExist(String username){
        try {
            Connection connection = PoolConnectionManager.getConnection();
            String sqlRequest = "SELECT EXISTS(SELECT 1 FROM users WHERE username=?);";
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e){
            LOGGER.error("Error checking the player's existence", e);
        }
        return false;
    }

    public static void addUserToTableUsers(String username){
        try {
            Connection connection = PoolConnectionManager.getConnection();
            String sqlRequest = "INSERT INTO users(username) VALUES(?);";
            PreparedStatement statement = connection.prepareStatement(sqlRequest);
            statement.setString(1, username);
            statement.executeUpdate();
        }catch (SQLException e){
            LOGGER.error("error adding user to table users", e);
        }
    }
}
