package repository.dsm;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class HexagonRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tableName;
    private final String sigIndexName = "hexagon_road_hexagon_id_index";

    public HexagonRoadRepository(String tableName) {
        this.tableName = tableName;
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            insertTable(conn);
            createSigCodeIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  road_id integer,\n"
            + "  hexagon_id bigint,\n"
            + "  sig_cd integer,\n"
            + "  CONSTRAINT fk_road FOREIGN KEY(road_id) REFERENCES road(id),\n"
            + "  CONSTRAINT fk_hexagon FOREIGN KEY(hexagon_id) REFERENCES hexagon(id))";
        executeQuery.create(conn, ddl);
    }

    private void insertTable(Connection conn) {
        String query = "INSERT INTO " + tableName
            + " SELECT r.origin_id, h.id, r.sig_cd\n"
            + " FROM road_segment as r, hexagon as h\n"
            + " WHERE ST_intersects(r.the_geom, h.the_geom)\n"
            + " GROUP BY r.origin_id, h.id, r.sig_cd";
        executeQuery.save(conn, query);
    }

    private void createSigCodeIndex(Connection conn) {
        String sql = "CREATE INDEX " + sigIndexName + " ON " + tableName + " USING btree(hexagon_id)";
        executeQuery.createIndex(conn, sql);
    }

    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER " + tableName + " USING " + sigIndexName;
        executeQuery.createIndex(conn, sql);
    }
}
