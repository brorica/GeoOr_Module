package repository.adminSector;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  the_geom geometry(MultiPolygon, 4326),\n"
            + "  adm_sect_cd integer,\n"
            + "  sgg_nm character varying(60),\n"
            + "  sgg_oid integer,\n"
            + "  col_adm_se character varying(5),\n"
            + "  gid integer primary key)";
        executeQuery.create(conn, ddl);
    }

    private void saveAdminSector(Connection conn, List<Shp> shps) {
        SaveAdminSector saveAdminSector = new SaveAdminSector();
        saveAdminSector.save(conn, shps);
    }

}
