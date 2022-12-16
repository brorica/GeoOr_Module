package repository.hazard.bridge;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;
import repository.ShpRepository;

public class BridgeRepository implements ShpRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String sigIndexName = "bridge_sig_cd_index";

    private final String tableName;

    public BridgeRepository(String tableName) {
        this.tableName = tableName;
    }

    @Override
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
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + " the_geom geometry(Point, 4326),\n"
            + " ufid varchar(34),\n"
            + " name varchar(100),\n"
            + " sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveBridge(Connection conn, List<Shp> shps) throws SQLException {
        SaveBridge saveBridge = new SaveBridge(tableName);
        saveBridge.save(conn, shps);
    }

    private void createIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, tableName, "btree", "sig_cd");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, tableName, sigIndexName);
    }
}
