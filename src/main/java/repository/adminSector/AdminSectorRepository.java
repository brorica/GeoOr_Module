package repository.adminSector;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

/**
 * 행정 구역 테이블을 만들고 저장하는 repository
 */
public class AdminSectorRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tableName;

    public AdminSectorRepository(String tableName) {
        this.tableName = tableName;
    }

    public void run(List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveAdminSector(conn, shps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  the_geom geometry(MultiPolygon, 4326) NOT NULL,\n"
            + "  sig_cd integer PRIMARY KEY NOT NULL)";
        executeQuery.create(conn, ddl);
    }

    private void saveAdminSector(Connection conn, List<Shp> shps) throws SQLException {
        SaveAdminSector saveAdminSector = new SaveAdminSector(tableName);
        saveAdminSector.save(conn, shps);
    }
}
