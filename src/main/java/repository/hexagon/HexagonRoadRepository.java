package repository.hexagon;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class HexagonRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String sigIndexName = "hexagon_road_hexagon_id_index";

    private final String originTableName;
    private final String tableName;

    public HexagonRoadRepository(String originTableName, String tableName) {
        this.originTableName = originTableName;
        this.tableName = tableName;
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            insertTable(conn);
            createSigCodeIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  road_segment_id integer,\n"
            + "  hexagon_id bigint,\n"
            + "  origin_road_id int,\n"
            + "  sig_cd integer,\n"
            + "  CONSTRAINT fk_road_segment FOREIGN KEY(road_segment_id) REFERENCES road_segment(id),\n"
            + "  CONSTRAINT fk_hexagon FOREIGN KEY(hexagon_id) REFERENCES " + originTableName + "(id))";
        executeQuery.create(conn, ddl);
    }

    private void insertTable(Connection conn) {
        String query = "INSERT INTO " + tableName
            + " SELECT r.id, h.id, r.origin_id, r.sig_cd\n"
            + " FROM road_segment as r, " + originTableName + " as h\n"
            + " WHERE ST_intersects(r.the_geom, h.the_geom)\n";
        executeQuery.save(conn, query);
    }

    private void createSigCodeIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, tableName, "btree", "hexagon_id");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, tableName, sigIndexName);
    }
}
