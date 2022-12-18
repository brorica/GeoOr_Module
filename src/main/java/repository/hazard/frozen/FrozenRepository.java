package repository.hazard.frozen;

import static config.ApplicationProperties.getProperty;

import config.JdbcTemplate;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

public class FrozenRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String sigIndexName = "frozen_sig_cd_index";
    private final String frozenTable = getProperty("frozen");

    public void run(List<File> files) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveFrozen(conn, files);
            createIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + frozenTable + " (\n"
            + "  the_geom geometry(Point, 4326),\n"
            + "  sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveFrozen(Connection conn, List<File> files) throws SQLException {
        SaveFrozen saveFrozen = new SaveFrozen(frozenTable);
        saveFrozen.save(conn, files);
    }

    private void createIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, frozenTable, "btree", "sig_cd");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, frozenTable, sigIndexName);
    }
}
