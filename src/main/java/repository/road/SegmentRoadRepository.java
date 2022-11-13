package repository.road;

import config.JdbcTemplate;
import domain.SqlReader;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class SegmentRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(SqlReader createSql) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            divideRoad(conn);
            createGeomIndex(conn);
            createSigCodeIndex(conn);
            createClusterIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        executeQuery.create(conn, sqlReader);
    }

    /**
     * 기존 도로를 일정 꼭짓점 이하가 되도록 쪼갠다.
     * 쪼개는 이유는 ST_Intersects 수행하는 과정에서 비교할 점의 수를 줄이기 위해서다.
     * 최대 꼭짓점이 줄어들 수록 쪼개지는 Polygon이 많아진다.
     * 현재는 최대 16개로 해놨다.
     * ST_Subdivide(좌표 타입, 최대 꼭짓점 수)
     */
    private void divideRoad(Connection conn) throws SQLException {
        String sql = "insert into road_segment "
            + "select id, sig_cd, ST_Subdivide(ST_MakeValid(the_geom), 16) from road";
        executeQuery.save(conn, sql);
    }

    private void createGeomIndex(Connection conn) {
        String sql = "CREATE INDEX road_geom_index ON road_segment USING gist(the_geom);";
        executeQuery.createIndex(conn, sql);
    }

    private void createSigCodeIndex(Connection conn) {
        String sql = "CREATE INDEX road_sig_cd_index ON road_segment USING btree(sig_cd);";
        executeQuery.createIndex(conn, sql);
    }

    /**
     * createSigCodeIndex 에서 시군구 코드를 기준으로 만들어진
     * 인덱스 기준으로 물리적 재정렬 실행
     */
    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER road_segment USING road_sig_cd_index";
        executeQuery.createIndex(conn, sql);
    }

}
