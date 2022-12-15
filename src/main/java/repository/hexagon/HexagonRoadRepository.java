package repository.hexagon;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class HexagonRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tempTableName = "temp_hexagon_road";
    private final String sigIndexName = "hexagon_road_hexagon_id_index";

    private final String originTableName;
    private final String tableName;

    public HexagonRoadRepository(String originTableName, String tableName) {
        this.originTableName = originTableName;
        this.tableName = tableName;
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTempTable(conn);
            createTable(conn);
            insertTempTable(conn);
            insertTable(conn);
            dropTempHexagonRoad(conn);
            createSigCodeIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTempTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tempTableName + " (\n"
            + "  hexagon_id bigint,\n"
            + "  road_id int)";
        executeQuery.create(conn, ddl);
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  hexagon_id bigint,\n"
            + "  road_id int,\n"
            + "  CONSTRAINT fk_road FOREIGN KEY(road_id) REFERENCES road(id),\n"
            + "  CONSTRAINT fk_hexagon FOREIGN KEY(hexagon_id) REFERENCES " + originTableName + "(id))";
        executeQuery.create(conn, ddl);
    }

    private void insertTempTable(Connection conn) {
        String query = "INSERT INTO " + tempTableName
            + " SELECT h.id, r.origin_id\n"
            + " FROM road_segment as r, " + originTableName + " as h\n"
            + " WHERE ST_intersects(r.the_geom, h.the_geom)\n";
        executeQuery.save(conn, query);
    }

    private void insertTable(Connection conn) {
        String query = "INSERT INTO " + tableName
            + " SELECT hexagon_id, road_id\n"
            + " FROM " + tempTableName + "\n"
            + " GROUP BY hexagon_id, road_id";
        executeQuery.save(conn, query);
    }

    private void dropTempHexagonRoad(Connection conn) {
        executeQuery.drop(conn, tempTableName);
    }

    private void createSigCodeIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, tableName, "btree", "hexagon_id");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, tableName, sigIndexName);
    }
}
