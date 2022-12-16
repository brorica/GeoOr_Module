package repository.hexagon;

import java.sql.Connection;
import repository.ExecuteQuery;

/**
 * 분할된 도로 polygon 테이블과 매칭하는 과정에서
 * cell id, road id가 중복돼서 들어가는 문제를 해결하기 위해 만듬
 * 이 테이블의 결과를 group by 해 hexagon_road 테이블에 저장한다.
 */
public class HexagonRoadTemp {

    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tableName = "temp_hexagon_road";

    private final String originTableName;

    public HexagonRoadTemp(String originTableName) {
        this.originTableName = originTableName;
    }

    public void run(Connection conn) {
        createTempTable(conn);
        insertTempTable(conn);

    }

    private void createTempTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  hexagon_id bigint,\n"
            + "  road_id int)";
        executeQuery.create(conn, ddl);
    }

    private void insertTempTable(Connection conn) {
        String query = "INSERT INTO " + tableName
            + " SELECT h.id, r.origin_id\n"
            + " FROM road_segment as r, " + originTableName + " as h\n"
            + " WHERE ST_intersects(r.the_geom, h.the_geom)\n";
        executeQuery.save(conn, query);
    }

}
