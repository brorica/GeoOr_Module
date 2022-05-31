package road.calcHillShade.service;

import road.calcHillShade.domain.HillShadeGrid;
import road.calcHillShade.domain.Road;
import road.calcHillShade.repository.FindOverlaps;
import road.calcHillShade.repository.GetRoads;
import road.calcHillShade.repository.UpdateRoadHillShade;
import config.JdbcTemplate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoadHillShadeService {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private List<Road> roads = new ArrayList<>();
    private int baseIndex;

    public void start(String sigCode, List<HillShadeGrid> hillShadeGrids) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            InitializeRoadArray(conn, sigCode);
            checkOverlaps(conn, hillShadeGrids, sigCode);
            updateRoadHillShade(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void InitializeRoadArray(Connection conn, String sigCode) throws SQLException {
        GetRoads getRoadCount = new GetRoads(sigCode);
        baseIndex = getRoadCount.get(conn, roads);
    }

    private void checkOverlaps(Connection conn, List<HillShadeGrid> hillShadeGrids, String sigCode) throws SQLException {
        // 테스트 데이터 삽입
        if (hillShadeGrids == null) {
            hillShadeGrids = createTestHillShadeGrids();
        }
        FindOverlaps findOverlaps = new FindOverlaps(sigCode);
        findOverlaps.match(conn, baseIndex, roads, hillShadeGrids);
    }

    private List<HillShadeGrid> createTestHillShadeGrids() {
        List<HillShadeGrid> hillShadeGrids = new ArrayList<>();
        String path = "./src/StoreDsmMain/resources/test/testGridPoints.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] points = line.split(" ");
                hillShadeGrids.add(new HillShadeGrid(
                    Double.parseDouble(points[0]),
                    Double.parseDouble(points[1]),
                    Double.parseDouble(points[2]),
                    Double.parseDouble(points[3]),
                    Double.parseDouble(points[4]),
                    Double.parseDouble(points[5])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hillShadeGrids;
    }

    private void updateRoadHillShade(Connection conn) throws SQLException {
        UpdateRoadHillShade updateRoadHillShade = new UpdateRoadHillShade();
        updateRoadHillShade.update(conn, roads);
    }
}
