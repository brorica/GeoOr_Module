package repository.dsm;

import config.JdbcTemplate;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;
import repository.FileRepository;

public class DsmRepository implements FileRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tableName;
    private final String indexName;

    public DsmRepository(String tableName) {
        this.tableName = tableName;
        this.indexName = "dsm_sig_cd_index";
    }

    @Override
    public void run(List<File> dsms) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            save(conn, dsms);
            deleteNullSigCode(conn);
            createIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "   x numeric not null,\n"
            + "   y numeric not null,\n"
            + "   z numeric not null,\n"
            + "   sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    /**
     * 주의 : 한 dsm 파일 처리 시간은 대략 6~13분 정도로 잡고 있으며,
     * 15분 이상 넘어가는 경우 자원 할당을 조정해야 함.
     * 단, 노트북의 경우 충전 유무에 따라 사용하는 cpu 양이 다르기 때문에
     * 충전기를 꼽으면 해결할 수 있다.
     */
    private void save(Connection conn, List<File> dsms) throws SQLException {
        System.out.println("# per 5 %");
        SaveDsm saveDsm = new SaveDsm(tableName);
        saveDsm.save(conn, dsms);
    }

    private void deleteNullSigCode(Connection conn) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE sig_cd is NULL";
        executeQuery.delete(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX " + indexName + " ON dsm USING btree(sig_cd)";
        executeQuery.createIndex(conn, sql);
    }

    /**
     * 이 쿼리는 기존의 dsm 테이블을 복제해 정렬하는 방식이기 때문에
     * 용량이 부족하다면 실행되지 않을 수 있다.
     */
    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER " + tableName + " USING "+ indexName;
        executeQuery.createIndex(conn, sql);
    }
}
