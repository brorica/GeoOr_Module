package repository.road;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

/**
 * 도로 테이블을 만들고 저장하는 repository
 */
public class RoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String tableName;

    public RoadRepository(String tableName) {
        this.tableName = tableName;
    }

    public void run(List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveRoad(conn, shps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "  id integer primary key generated always as identity,\n"
            + "  the_geom geometry(MultiPolygon, 4326) NOT NULL,\n"
            + "  sig_cd integer NOT NULL,\n"
            + "  hillshade integer default 0)";
        executeQuery.create(conn, ddl);
    }

    private void saveRoad(Connection conn, List<Shp> shps) {
        SaveRoad saveRoad = new SaveRoad(tableName);
        saveRoad.save(conn, shps);
    }
}
