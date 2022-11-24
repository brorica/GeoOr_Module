package repository.road;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class SegmentRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String originTableName;
    private final String segmentTableName;
    private final String geomIndexName;
    private final String sigIndexName;

    public SegmentRoadRepository(String originTableName, String segmentTableName) {
        this.originTableName = originTableName;
        this.segmentTableName = segmentTableName;
        this.geomIndexName = "road_geom_index";
        this.sigIndexName = "road_sig_cd_index";
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            divideRoad(conn);
            createGeomIndex(conn);
            createSigCodeIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + segmentTableName + " (\n"
            + "  origin_id integer,\n"
            + "  sig_cd integer,\n"
            + "  the_geom geometry(Polygon, 4326))";
        executeQuery.create(conn, ddl);
    }

    private void divideRoad(Connection conn) throws SQLException {
        String sql = "insert into " + segmentTableName +
            "select id, sig_cd, ST_Subdivide(ST_MakeValid(the_geom), 64) from " + originTableName;
        executeQuery.save(conn, sql);
    }

    private void createGeomIndex(Connection conn) {
        String sql = "CREATE INDEX " + geomIndexName +" ON " + segmentTableName + " USING gist(the_geom)";
        executeQuery.createIndex(conn, sql);
    }

    private void createSigCodeIndex(Connection conn) {
        String sql = "CREATE INDEX " + sigIndexName + " ON " + segmentTableName + " USING btree(sig_cd)";
        executeQuery.createIndex(conn, sql);
    }

    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER " + segmentTableName + " USING " + sigIndexName;
        executeQuery.createIndex(conn, sql);
    }

}
