package repository.hexagon;

import java.sql.Connection;
import repository.ExecuteQuery;

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
