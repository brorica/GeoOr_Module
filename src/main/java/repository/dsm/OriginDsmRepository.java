package repository.dsm;

import config.JdbcTemplate;
import domain.SqlReader;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class OriginDsmRepository {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(SqlReader createSql, File[] dsms) {
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

}
