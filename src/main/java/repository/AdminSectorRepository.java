package repository;

import config.JdbcTemplate;
import domain.Shp;
import domain.SqlReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.adminSector.SaveAdminSector;

public class AdminSectorRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void saveOriginData(SqlReader createSql, List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            saveAdminSector(conn, shps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        executeQuery.create(conn, sqlReader);
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
        String sql = "insert into admin_sector_divide(the_geom, adm_sect_cd) "
            + "select ST_Subdivide(ST_CollectionExtract(ST_MakeValid(the_geom), 3)), adm_sect_cd from admin_sector";
        executeQuery.save(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX admin_sector_index ON admin_sector_divide USING gist(the_geom);";
        executeQuery.createIndex(conn, sql);
    }

}
