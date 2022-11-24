package repository.bridge;

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

    private final String tableName;
    private final String sigIndexName;

    public BridgeRepository(String tableName) {
        this.tableName = tableName;
        this.sigIndexName = "bridge_sig_cd_index";
    }

    @Override
    public void run(List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveBridge(conn, shps);
            createIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + " the_geom geometry(Point, 4326),\n"
            + " ufid varchar(34),\n"
            + " kind varchar(6),\n"
            + " name varchar(100),\n"
            + " leng numeric(7, 2),\n"
            + " widt numeric(5, 2),\n"
            + " eymd varchar(30),\n"
            + " qual varchar(6),\n"
            + " rvnm varchar(30),\n"
            + " rest varchar(50),\n"
            + " scls varchar(8),\n"
            + " fmta varchar(9),\n"
            + " sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveBridge(Connection conn, List<Shp> shps) throws SQLException {
        SaveBridge saveBridge = new SaveBridge(tableName);
        saveBridge.save(conn, shps);
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
