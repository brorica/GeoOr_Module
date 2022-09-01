package repository;

import config.JdbcTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import domain.SqlReader;
import repository.dsm.CreatePrimaryKey;
import repository.dsm.DeleteUnUseDsm;
import repository.dsm.SaveDsmTemp;

public class DsmRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void saveOriginData(SqlReader createSql, File[] dsms) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            saveDsmTemp(conn, dsms);
            deleteNullSigCd(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        RunScript runScript = new RunScript();
        runScript.create(conn, sqlReader);
    }

    private void saveDsmTemp(Connection conn, File[] dsms) throws SQLException {
        System.out.println("# per 5 %");
        SaveDsmTemp saveDsmTemp = new SaveDsmTemp();
        saveDsmTemp.save(conn, dsms);;
    }

    private void deleteNullSigCd(Connection conn) throws SQLException {
        DeleteUnUseDsm deleteUnUseDsm = new DeleteUnUseDsm();
        deleteUnUseDsm.delete(conn);
    }

    public void procOriginData(SqlReader createSql) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            dropDsmTempTable(conn);
            setPrimaryKey(conn);
            createIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropDsmTempTable(Connection conn) {
        DeleteUnUseDsm deleteUnUseDsm = new DeleteUnUseDsm();
        deleteUnUseDsm.drop(conn);
    }

    private void setPrimaryKey(Connection conn) {
        CreatePrimaryKey createPrimaryKey = new CreatePrimaryKey();
        createPrimaryKey.create(conn);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX dsm_sig_cd_index ON dsm USING brin(sig_cd);";
        CreateIndex createIndex = new CreateIndex();
        createIndex.create(conn, sql);
    }
}
