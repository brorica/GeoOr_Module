package repository;

import config.JdbcTemplate;
import domain.Shp;
import domain.SqlReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.adminSector.DividePolygon;
import repository.adminSector.SaveAdminSector;

public class AdminSectorRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void saveOriginData(SqlReader createSql, List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            saveAdminSector(conn, shps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        RunScript runScript = new RunScript();
        runScript.create(conn, sqlReader);
    }

    private void saveAdminSector(Connection conn, List<Shp> shps) {
        SaveAdminSector saveAdminSector = new SaveAdminSector();
        saveAdminSector.save(conn, shps);
    }

    public void procOriginData(SqlReader createSql) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            divideAdminSector(conn);
            createIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void divideAdminSector(Connection conn) throws SQLException {
        DividePolygon dividePolygon = new DividePolygon();
        dividePolygon.divide(conn);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX admin_sector_index ON admin_sector_divide USING gist(the_geom);";
        CreateIndex createIndex = new CreateIndex();
        createIndex.create(conn, sql);
    }

}
