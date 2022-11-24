package repository.tunnel;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;
import repository.ShpRepository;

public class TunnelRepository implements ShpRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tableName;
    private final String sigIndexName;

    public TunnelRepository(String tableName) {
        this.tableName = tableName;
        this.sigIndexName = "tunnel_sig_cd_index";
    }

    @Override
    public void run(List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveTunnel(conn, shps);
            createIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + " the_geom geometry(Point, 5179),\n"
            + " ufid varchar(32) not null PRIMARY KEY,\n"
            + " name varchar(100),\n"
            + " leng numeric(7, 2),\n"
            + " widt numeric(5, 2),\n"
            + " heig numeric(5, 2),\n"
            + " scls varchar(8),\n"
            + " fmta varchar(9),\n"
            + " sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveTunnel(Connection conn, List<Shp> shps) throws SQLException {
        SaveTunnel saveTunnel = new SaveTunnel(tableName);
        saveTunnel.save(conn, shps);
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
