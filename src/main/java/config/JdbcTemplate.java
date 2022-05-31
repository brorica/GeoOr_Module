package config;

import static config.ApplicationProperties.getProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTemplate {
    private final String connectUrl;
    private final String user;
    private final String password;

    public JdbcTemplate() {
        connectUrl = getProperty("jdbc.connectUrl");
        user = getProperty("jdbc.user");
        password = getProperty("jdbc.password");
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = DriverManager.getConnection(connectUrl, user, password);
        conn.setAutoCommit(false);
        return conn;
    }
}
