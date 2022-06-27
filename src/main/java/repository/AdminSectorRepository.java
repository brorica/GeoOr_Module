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

    public void runSQL(SqlReader sqlReader) {
        TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAdminSector(List<Shp> shps) {
        SaveAdminSector saveAdminSector = new SaveAdminSector();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveAdminSector.save(conn, shps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createIndex(SqlReader sqlReader) {
        CreateIndex createIndex = new CreateIndex();
        try (Connection conn = jdbcTemplate.getConnection()) {
            createIndex.create(conn, sqlReader);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
