package repository.adminSector;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.RelateAdminSector;
import repository.ExecuteQuery;

public class SegmentRelateAdminSectorRepository extends RelateAdminSector {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String originTableName;
    private final String geomIndexName;
    private final int maximumPoints;

    public SegmentRelateAdminSectorRepository(String originTableName) {
        this.originTableName = originTableName;
        this.geomIndexName = "admin_sector_index";
        this.maximumPoints = 64;
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
        String ddl = "CREATE TABLE IF NOT EXISTS " + getAdminSectorSegmentTableName() + " (\n"
            + "  the_geom geometry(Polygon, 4326),\n"
            + "  adm_sect_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void divideAdminSector(Connection conn) throws SQLException {
        String sql = "insert into " + getAdminSectorSegmentTableName()
            + " select ST_Subdivide(the_geom, " + maximumPoints +"), adm_sect_cd from " + originTableName;
        executeQuery.save(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX " + geomIndexName + " ON " + getAdminSectorSegmentTableName() + " USING gist(the_geom)";
        executeQuery.createIndex(conn, sql);
    }
}
