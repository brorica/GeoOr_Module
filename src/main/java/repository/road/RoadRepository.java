package repository.road;

import config.JdbcTemplate;
import domain.Shp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

public class RoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveRoad(conn, shps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS road (\n"
            + "  id integer primary key generated always as identity,\n"
            + "  the_geom geometry(MultiPolygon, 4326),\n"
            + "  opert_de character varying(14),\n"
            + "  rw_sn double precision,\n"
            + "  sig_cd integer,\n"
            + "  hillshade integer default 0)";
        executeQuery.create(conn, ddl);
    }

    private void saveRoad(Connection conn, List<Shp> shps) {
        SaveRoad saveRoad = new SaveRoad();
        saveRoad.save(conn, shps);
    }

}
