package repository.hazard.bridge;

import static config.ApplicationProperties.getProperty;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

public class BridgeRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String sigIndexName = "bridge_sig_cd_index";
    private final String bridgeTable = getProperty("bridge");

    public void run(List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveBridge(conn, shps);
            createIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + bridgeTable + " (\n"
            + " the_geom geometry(Point, 4326),\n"
            + " ufid varchar(34),\n"
            + " name varchar(100),\n"
            + " sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveBridge(Connection conn, List<Shp> shps) throws SQLException {
        SaveBridge saveBridge = new SaveBridge(bridgeTable);
        saveBridge.save(conn, shps);
    }

    private void createIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, bridgeTable, "btree", "sig_cd");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, bridgeTable, sigIndexName);
    }
}
