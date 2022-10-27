package repository.dsm;

import config.JdbcTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import domain.SqlReader;
import repository.ExecuteQuery;
import repository.dsm.SaveDsmTemp;

public class DsmRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

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
        executeQuery.create(conn, sqlReader);
    }

    private void saveDsmTemp(Connection conn, File[] dsms) throws SQLException {
        System.out.println("# per 5 %");
        SaveDsmTemp saveDsmTemp = new SaveDsmTemp();
        saveDsmTemp.save(conn, dsms);;
    }

    private void deleteNullSigCd(Connection conn) throws SQLException {
        String sql = "DELETE FROM dsm_temp WHERE sig_cd is NULL";
        executeQuery.delete(conn, sql);
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
        String sql = "DROP table dsm_temp";
        executeQuery.drop(conn, sql);
    }

    private void setPrimaryKey(Connection conn) {
        String sql = "ALTER TABLE dsm ADD COLUMN id SERIAL PRIMARY KEY;";
        executeQuery.alter(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX dsm_sig_cd_index ON dsm USING brin(sig_cd);";
        executeQuery.createIndex(conn, sql);
    }
}
