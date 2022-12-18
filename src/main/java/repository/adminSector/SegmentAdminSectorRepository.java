package repository.adminSector;

import static config.ApplicationProperties.getProperty;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

/**
 * 기존 행정 구역 테이블은 r-tree 탐색에 적합한 형태가 아니기 때문에
 * 내부적으로 분할해야 한다.
 */
public class SegmentAdminSectorRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String adminTable = getProperty("admin");
    private final String adminSegmentTable = getProperty("admin.segment");
    private final int maximumPoints = 64;
    private final String geomIndexName = "admin_sector_index";

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
        String sql = "insert into " + adminSegmentTable
            + " select ST_Subdivide(ST_MakeValid(the_geom), " + maximumPoints
            + "), sig_cd from " + adminTable;
        executeQuery.save(conn, sql);
    }

    private void createIndex(Connection conn) {
        executeQuery.createIndex(conn, geomIndexName, adminSegmentTable, "gist", "the_geom");
    }
}
