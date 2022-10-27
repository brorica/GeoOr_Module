package repository;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import domain.Shp;
import domain.SqlReader;
import repository.road.DividePolygon;
import repository.road.SaveRoad;

public class RoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void saveOriginData(SqlReader createSql, List<Shp> shps) {
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

    public void procOriginData(SqlReader createSql) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            divideDumpedRoad(conn);
            createIndex(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void divideDumpedRoad(Connection conn) throws SQLException {
        DividePolygon dividePolygon = new DividePolygon();
        dividePolygon.divide(conn);
    }

    private void createIndex(Connection conn) {
        String sql = "CREATE INDEX road_index ON road_divide USING gist(the_geom);";
        executeQuery.createIndex(conn, sql);
    }

}
