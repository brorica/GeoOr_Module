package repository;

import config.JdbcTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import domain.SqlReader;
import repository.dsm.DeleteUnUseDsm;
import repository.dsm.SaveDsm;
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
    // 그냥 dsm 넣는 코드
    public void save(File[] dsms) {
        SaveDsm saveDsm = new SaveDsm();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveDsm.save(conn, dsms);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 이 메소드는 dsm을 넣으면서 행정 구역도 찾아서 넣는 메소드다.
     * 매우 오래 걸린다.
     */
    public void saveOverlaps(File[] dsms) {
        SaveDsmOverlpas saveDsmOverlpas = new SaveDsmOverlpas();
        try (Connection conn = jdbcTemplate.getConnection()) {
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
