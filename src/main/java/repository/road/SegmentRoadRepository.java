package repository.road;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class SegmentRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String geomIndexName = "road_geom_index";
    private final int maximumPoints = 64;

    private final String originTableName;
    private final String segmentTableName;

    public SegmentRoadRepository(String originTableName, String segmentTableName) {
        this.originTableName = originTableName;
        this.segmentTableName = segmentTableName;
    }

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
        String ddl = "CREATE TABLE IF NOT EXISTS " + segmentTableName + " (\n"
            + "  origin_id integer,\n"
            + "  sig_cd integer NOT NULL,\n"
            + "  the_geom geometry(Polygon, 4326) NOT NULL)";
        executeQuery.create(conn, ddl);
    }

    /**
     * 주의 : 이 작업은 전국 도로 데이터를 전부 넣는다 할 때, 매우 오래 걸림
     */
    private void divideRoad(Connection conn) throws SQLException {
        String sql = "insert into " + segmentTableName +
            " select id, sig_cd, ST_Subdivide(ST_MakeValid(the_geom), " + maximumPoints + ") from " + originTableName;
        executeQuery.save(conn, sql);
    }

    private void createGeomIndex(Connection conn) {
        executeQuery.createIndex(conn, geomIndexName, segmentTableName, "gist", "the_geom");
    }
}
