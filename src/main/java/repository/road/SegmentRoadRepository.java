package repository.road;

import static config.ApplicationProperties.getProperty;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

/**
 * 기존 도로 테이블은 r-tree 탐색에 적합한 형태가 아니기 때문에
 * 내부적으로 분할해야 한다.
 */
public class SegmentRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String originTable = getProperty("road");
    private final String segmentTable = getProperty("road.segment");
    private final int maximumPoints = 64;
    private final String geomIndexName = "road_geom_index";

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            divideRoad(conn);
            createGeomIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + segmentTable + " (\n"
            + "  origin_id integer,\n"
            + "  sig_cd integer NOT NULL,\n"
            + "  the_geom geometry(Polygon, 4326) NOT NULL)";
        executeQuery.create(conn, ddl);
    }

    /**
     * 주의 : 이 작업은 전국 도로 데이터를 전부 넣는다 할 때, 매우 오래 걸림
     */
    private void divideRoad(Connection conn) throws SQLException {
        String sql = "insert into " + segmentTable +
            " select id, sig_cd, ST_Subdivide(ST_MakeValid(the_geom), " + maximumPoints + ") from " + originTable;
        executeQuery.save(conn, sql);
    }

    private void createGeomIndex(Connection conn) {
        executeQuery.createIndex(conn, geomIndexName, segmentTable, "gist", "the_geom");
    }
}
