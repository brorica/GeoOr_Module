package repository;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import domain.Shp;
import domain.SqlReader;
import repository.road.CreateIndex;
import repository.road.SaveRoad;
import repository.road.SaveRoadCentroid;

public class RoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void createRoadTable(SqlReader sqlReader) {
        TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createRoadCentroidTable(SqlReader sqlReader) {
        TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveRoad(List<Shp> shps) {
        SaveRoad saveRoad = new SaveRoad();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveRoad.save(conn, shps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveRoadCentroid(List<Shp> shps) {
        SaveRoadCentroid saveRoadCentroid = new SaveRoadCentroid();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveRoadCentroid.save(conn, shps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createIndex() {
        CreateIndex createIndex = new CreateIndex();
        try (Connection conn = jdbcTemplate.getConnection()) {
            createIndex.create(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
