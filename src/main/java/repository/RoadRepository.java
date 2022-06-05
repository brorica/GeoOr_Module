package repository;

import config.JdbcTemplate;
import domain.HillShadeGrid;
import domain.Road;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import domain.Shp;
import domain.SqlReader;
import repository.road.FindOverlaps;
import repository.road.GetRoads;
import repository.road.SaveRoad;
import repository.road.UpdateRoadHillShade;

public class RoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void createTable(SqlReader sqlReader) {
        TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(List<Shp> shps) {
        SaveRoad saveRoad = new SaveRoad();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveRoad.save(conn, shps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void findByOverlapGrid(String sigCode, int baseIndex, List<Road> roads,
        List<HillShadeGrid> hillShadeGrids) {
        FindOverlaps findOverlaps = new FindOverlaps(sigCode);
        try (Connection conn = jdbcTemplate.getConnection()) {
            findOverlaps.match(conn, baseIndex, roads, hillShadeGrids);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findBySigCode(String sigCode, List<Road> roads) {
        GetRoads getRoads = new GetRoads(sigCode);
        int baseIndex = 0;
        try (Connection conn = jdbcTemplate.getConnection()) {
            baseIndex = getRoads.get(conn, roads);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return baseIndex;
    }

    public void updateHillShade(List<Road> roads) {
        UpdateRoadHillShade updateRoadHillShade = new UpdateRoadHillShade();
        try (Connection conn = jdbcTemplate.getConnection()) {
            updateRoadHillShade.update(conn, roads);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
