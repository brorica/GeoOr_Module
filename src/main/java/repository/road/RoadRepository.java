package repository.road;

import config.JdbcTemplate;
import domain.Shp;
import domain.SqlReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

public class RoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(SqlReader createSql, List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            saveRoad(conn, shps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        executeQuery.create(conn, sqlReader);
    }

    private void saveRoad(Connection conn, List<Shp> shps) {
        SaveRoad saveRoad = new SaveRoad();
        saveRoad.save(conn, shps);
    }

}
