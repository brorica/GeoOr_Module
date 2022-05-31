package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTemplate {
    private final String connectUrl;
    private final String user;
    private final String password;

    public JdbcTemplate() {
        connectUrl = System.getenv("jdbc.connectUrl");
        user = System.getenv("jdbc.user");
        password = System.getenv("jdbc.password");
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(System.getenv("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = DriverManager.getConnection(connectUrl, user, password);
        conn.setAutoCommit(false);
        return conn;
    }
}
