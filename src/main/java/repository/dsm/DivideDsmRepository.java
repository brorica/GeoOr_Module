package repository.dsm;

import config.JdbcTemplate;
import domain.SqlReader;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class DivideDsmRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(SqlReader createSql) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            dropDsmTempTable(conn);
            setPrimaryKey(conn);
            createIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        executeQuery.create(conn, sqlReader);
    }

    private void dropDsmTempTable(Connection conn) {
        String sql = "DROP table dsm_temp";
        executeQuery.drop(conn, sql);
    }

    private void setPrimaryKey(Connection conn) {
        String sql = "ALTER TABLE dsm ADD COLUMN id SERIAL PRIMARY KEY;";
        executeQuery.alter(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX dsm_sig_cd_index ON dsm USING brin(sig_cd);";
        executeQuery.createIndex(conn, sql);
    }

}
