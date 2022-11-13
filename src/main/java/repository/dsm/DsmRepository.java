package repository.dsm;

import config.JdbcTemplate;
import domain.SqlReader;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class DsmRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(File[] dsms) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveDsmTemp(conn, dsms);
            deleteNullSigCode(conn);
            createIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS dsm (\n"
            + "   x numeric not null,\n"
            + "   y numeric not null,\n"
            + "   z numeric not null,\n"
            + "   sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveDsmTemp(Connection conn, File[] dsms) throws SQLException {
        System.out.println("# per 5 %");
        SaveDsm saveDsm = new SaveDsm();
        saveDsm.save(conn, dsms);
        ;
    }

    private void deleteNullSigCode(Connection conn) throws SQLException {
        String sql = "DELETE FROM dsm WHERE sig_cd is NULL";
        executeQuery.delete(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX dsm_sig_cd_index ON dsm USING btree(sig_cd);";
        executeQuery.createIndex(conn, sql);
    }

    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER dsm USING dsm_sig_cd_index";
        executeQuery.createIndex(conn, sql);
    }

}
