package repository.frozen;

import config.JdbcTemplate;
import domain.SqlReader;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class FrozenRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(SqlReader createSql, File[] files) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            saveFrozen(conn, files);
            createIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader createSql) {
        executeQuery.create(conn, createSql);
    }

    private void saveFrozen(Connection conn, File[] files) throws SQLException {
        SaveFrozen saveFrozen = new SaveFrozen();
        saveFrozen.save(conn, files);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX frozen_sig_cd_index ON frozen USING btree(sig_cd);";
        executeQuery.createIndex(conn, sql);
    }

    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER frozen USING frozen_sig_cd_index";
        executeQuery.createIndex(conn, sql);
    }

}
