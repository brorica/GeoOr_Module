package repository.frozen;

import config.JdbcTemplate;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;
import repository.FileRepository;

public class FrozenRepository implements FileRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tableName;
    private final String sigIndexName;

    public FrozenRepository(String tableName) {
        this.tableName = tableName;
        this.sigIndexName = "frozen_sig_cd_index";
    }

    @Override
    public void run(List<File> files) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveFrozen(conn, files);
            createIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  the_geom geometry(Point, 4326),\n"
            + "  sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveFrozen(Connection conn, List<File> files) throws SQLException {
        SaveFrozen saveFrozen = new SaveFrozen(tableName);
        saveFrozen.save(conn, files);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX " + sigIndexName + " ON " + tableName + " USING btree(sig_cd)";
        executeQuery.createIndex(conn, sql);
    }

    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER " + tableName + " USING " + sigIndexName;
        executeQuery.createIndex(conn, sql);
    }
}
