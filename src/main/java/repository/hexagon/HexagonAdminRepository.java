package repository.hexagon;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

/**
 * hexagon과 행정 구역간의 정보를모아둠
 */
public class HexagonAdminRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String sigIndexName = "hexagon_admin_sig_cd_index";

    private final String originTableName;
    private final String tableName;


    public HexagonAdminRepository(String originTableName, String tableName) {
        this.originTableName = originTableName;
        this.tableName = tableName;
    }

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            insertTable(conn);
            createSigCodeIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  sig_cd integer,\n"
            + "  hexagon_id bigint,\n"
            + "  CONSTRAINT fk_admin_sector FOREIGN KEY(sig_cd) REFERENCES admin_sector(sig_cd),\n"
            + "  CONSTRAINT fk_hexagon FOREIGN KEY(hexagon_id) REFERENCES " + originTableName +"(id))";
        executeQuery.create(conn, ddl);
    }

    /**
     * 이 쿼리를 성공적으로 수행하기 위해선
     * 행정 구역에 대한 정보가 있어야 한다.
     */
    private void insertTable(Connection conn) {
        String query = "INSERT INTO " + tableName
            + " SELECT a.sig_cd, h.id\n"
            + " FROM admin_sector_segment as a, " + originTableName + " as h\n"
            + " where ST_INTERSECTS(a.the_geom, h.the_geom)\n"
            + " group by a.sig_cd, h.id";
        executeQuery.save(conn, query);
    }

    private void createSigCodeIndex(Connection conn) {
        executeQuery.createIndex(conn, sigIndexName, tableName, "btree", "sig_cd");
    }

    private void createClusterIndex(Connection conn) {
        executeQuery.createIndex(conn, tableName, sigIndexName);
    }
}
