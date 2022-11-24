package repository.adminSector;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

/**
 * AdminSectorRepository 에서 저장한 행정구역을 분할해 인덱싱
 */

public class SegmentAdminSectorRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String originTableName;
    private final String segmentTableName;
    private final String geomIndexName;

    public SegmentAdminSectorRepository(String originTableName, String segmentTableName) {
        this.originTableName = originTableName;
        this.segmentTableName = segmentTableName;
        this.geomIndexName = "admin_sector_index";
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            divideAdminSector(conn);
            createIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + segmentTableName + " (\n"
            + "  the_geom geometry(Polygon, 4326),\n"
            + "  adm_sect_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void divideAdminSector(Connection conn) throws SQLException {
        String sql = "insert into " + segmentTableName
            + "select ST_Subdivide(the_geom, 64), adm_sect_cd from " + originTableName;
        executeQuery.save(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX " + geomIndexName + " ON " + segmentTableName + " USING gist(the_geom)";
        executeQuery.createIndex(conn, sql);
    }
}
