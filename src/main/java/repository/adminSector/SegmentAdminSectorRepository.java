package repository.adminSector;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class SegmentAdminSectorRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String originTableName;
    private final String segmentTableName;
    private final String geomIndexName;
    private final int maximumPoints;

    public SegmentAdminSectorRepository(String originTableName, String segmentTableName) {
        this.originTableName = originTableName;
        this.segmentTableName = segmentTableName;
        this.geomIndexName = "admin_sector_index";
        this.maximumPoints = 64;
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            divideAdminSector(conn);
            createIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + "admin_sector_segment" + " (\n"
            + "  the_geom geometry(Polygon, 4326),\n"
            + "  sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void divideAdminSector(Connection conn) throws SQLException {
        String sql = "insert into " + segmentTableName
            + " select ST_Subdivide(ST_MakeValid(the_geom), " + maximumPoints
            + "), sig_cd from " + originTableName;
        executeQuery.save(conn, sql);
    }

    private void createIndex(Connection conn) {
        executeQuery.createIndex(conn, geomIndexName, segmentTableName, "gist", "the_geom");
    }
}
