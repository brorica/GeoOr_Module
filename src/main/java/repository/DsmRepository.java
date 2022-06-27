package repository;

import config.JdbcTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import domain.SqlReader;
import repository.dsm.DeleteUnUseDsm;
import repository.dsm.SaveDsmTemp;
import repository.dsm.SaveSortedDsm;

public class DsmRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void createTable(SqlReader sqlReader) {
        RunScript runScript = new RunScript();
        try (Connection conn = jdbcTemplate.getConnection()) {
            runScript.create(conn, sqlReader);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDsmTemp(File[] dsms) {
        SaveDsmTemp saveDsmTemp = new SaveDsmTemp();
        try (Connection conn = jdbcTemplate.getConnection()) {
            System.out.println("# per 5 %");
            saveDsmTemp.save(conn, dsms);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNullSigCd() {
        DeleteUnUseDsm deleteUnUseDsm = new DeleteUnUseDsm();
        try (Connection conn = jdbcTemplate.getConnection()) {
            deleteUnUseDsm.delete(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSortedDsm(SqlReader sqlReader) {
        SaveSortedDsm saveSortedDsm = new SaveSortedDsm();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveSortedDsm.save(conn, sqlReader);
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

    public void dropDsmTempTable() {
        DeleteUnUseDsm deleteUnUseDsm = new DeleteUnUseDsm();
        try (Connection conn = jdbcTemplate.getConnection()) {
            deleteUnUseDsm.drop(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
