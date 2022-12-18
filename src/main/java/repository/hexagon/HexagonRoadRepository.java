package repository.hexagon;

import static config.ApplicationProperties.getProperty;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

/**
 * h3과 도로 테이블은 다대다 관계이기 때문에
 * 중간에 테이블을 놔야 한다.
 */
public class HexagonRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tempTable = "temp_hexagon_road";
    private final String sigIndexName = "hexagon_road_hexagon_id_index";
    private final String hexagonTable = getProperty("h3.hexagon");
    private final String hexagonRoadTable = getProperty("h3.road");

    private final HexagonRoadTemp hexagonRoadTemp;

    public HexagonRoadRepository() {
        this.hexagonRoadTemp = new HexagonRoadTemp(hexagonTable, tempTable);
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            hexagonRoadTemp.run(conn);
            createTable(conn);
            insertTable(conn);
            createSigCodeIndex(conn);
            createClusterIndex(conn);
            dropTempHexagonRoad(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + hexagonRoadTable + " (\n"
            + "  hexagon_id bigint,\n"
            + "  road_id int,\n"
            + "  CONSTRAINT fk_road FOREIGN KEY(road_id) REFERENCES " + getProperty("road") + "(id),\n"
            + "  CONSTRAINT fk_hexagon FOREIGN KEY(hexagon_id) REFERENCES " + hexagonTable + "(id))";
        executeQuery.create(conn, ddl);
    }

    private void insertTable(Connection conn) {
        String query = "INSERT INTO " + hexagonRoadTable
            + " SELECT hexagon_id, road_id\n"
            + " FROM " + tempTable + "\n"
            + " GROUP BY hexagon_id, road_id";
        executeQuery.save(conn, query);
    }

    private void createSigCodeIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, hexagonRoadTable, "btree", "hexagon_id");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, hexagonRoadTable, sigIndexName);
    }

    private void dropTempHexagonRoad(Connection conn) {
        executeQuery.drop(conn, tempTable);
    }
}
