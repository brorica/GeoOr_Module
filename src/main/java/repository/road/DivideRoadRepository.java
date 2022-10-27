package repository.road;

import config.JdbcTemplate;
import domain.SqlReader;
import java.sql.Connection;
import java.sql.SQLException;
import repository.ExecuteQuery;

public class DivideRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(SqlReader createSql) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            divideDumpedRoad(conn);
            createIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        executeQuery.create(conn, sqlReader);
    }

    private void divideDumpedRoad(Connection conn) throws SQLException {
        String sql = "insert into road_divide(origin_id, the_geom)"
            + " select id, ST_Subdivide(ST_CollectionExtract(ST_MakeValid(the_geom), 3)) from road";
        executeQuery.save(conn, sql);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX road_index ON road_divide USING gist(the_geom);";
        executeQuery.createIndex(conn, sql);
    }

}
