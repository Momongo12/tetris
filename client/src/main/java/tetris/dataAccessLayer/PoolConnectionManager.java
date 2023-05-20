package tetris.dataAccessLayer;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class PoolConnectionManager {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydatabase";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Mama204512";
    private static final int MAX_TOTAL_CONNECTIONS = 10;
    private static final int MAX_IDLE_CONNECTIONS = 5;

    private static BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        dataSource.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        dataSource.setMaxIdle(MAX_IDLE_CONNECTIONS);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}