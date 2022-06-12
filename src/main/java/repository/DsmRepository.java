package repository;

import config.JdbcTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import domain.SqlReader;
import repository.dsm.DeleteUnUseDsm;
import repository.dsm.SaveDsmOverlpas;

public class DsmRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void createTable(SqlReader sqlReader) {
        TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveOverlaps(File[] dsms) {
        SaveDsmOverlpas saveDsmOverlpas = new SaveDsmOverlpas();
        try (Connection conn = jdbcTemplate.getConnection()) {
            System.out.println("# per 5 %");
            saveDsmOverlpas.save(conn, dsms);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        DeleteUnUseDsm deleteUnUseDsm = new DeleteUnUseDsm();
        try (Connection conn = jdbcTemplate.getConnection()) {
            deleteUnUseDsm.delete(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
