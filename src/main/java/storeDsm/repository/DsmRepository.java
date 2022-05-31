package storeDsm.repository;

import config.JdbcTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import util.CreateSqlReader;

public class DsmRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void createTable(CreateSqlReader sqlReader) {
        TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(File[] dsms) {
        SaveDsm saveDsm = new SaveDsm();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveDsm.save(conn, dsms);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
