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
        String ddl = "CREATE TABLE IF NOT EXISTS admin_sector_segment (\n"
            + "  the_geom geometry(Polygon, 4326),\n"
            + "  adm_sect_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void divideAdminSector(Connection conn) throws SQLException {
        String sql = "insert into admin_sector_segment "
            + "select ST_Subdivide(the_geom, 16), adm_sect_cd from admin_sector";
        executeQuery.save(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX admin_sector_index ON admin_sector_segment USING gist(the_geom);";
        executeQuery.createIndex(conn, sql);
    }
}
