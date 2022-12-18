package repository.hazard.tunnel;

import static config.ApplicationProperties.getProperty;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

public class TunnelRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String sigIndexName = "tunnel_sig_cd_index";
    private final String tunnelTable = getProperty("tunnel");

    public void run(List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveTunnel(conn, shps);
            createIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tunnelTable + " (\n"
            + " the_geom geometry(Point, 4326),\n"
            + " ufid varchar(32) not null PRIMARY KEY,\n"
            + " name varchar(100),\n"
            + " sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveTunnel(Connection conn, List<Shp> shps) throws SQLException {
        SaveTunnel saveTunnel = new SaveTunnel(tunnelTable);
        saveTunnel.save(conn, shps);
    }

    private void createIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, tunnelTable, "btree", "sig_cd");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, tunnelTable, sigIndexName);
    }
}
