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

    private void divideRoad(Connection conn) throws SQLException {
        String sql = "insert into road_divide(origin_id, sig_cd, the_geom)"
            + " select id, sig_cd, ST_Subdivide(ST_CollectionExtract(ST_MakeValid(the_geom), 3)) from road";
        executeQuery.save(conn, sql);
    }

    private void createGeomIndex(Connection conn) {
        String sql = "CREATE INDEX road_geom_index ON road_divide USING gist(the_geom);";
        executeQuery.createIndex(conn, sql);
    }

    private void createSigCodeIndex(Connection conn) {
        String sql = "CREATE INDEX road_sig_cd_index ON road_divide USING btree(sig_cd);";
        executeQuery.createIndex(conn, sql);
    }

    private void createClusterIndex(Connection conn) {
        String sql = "CLUSTER road_divide USING road_sig_cd_index";
        executeQuery.createIndex(conn, sql);
    }

}
