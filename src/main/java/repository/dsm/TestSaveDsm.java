package repository.dsm;

import com.uber.h3core.util.LatLng;
import geoUtil.UberH3;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestSaveDsm {

    private final int batchLimitValue = 648000;
    private final WKB wkb = new WKB();
    private final String tableName;
    private final UberH3 h3;

    private int count = 0;

    public TestSaveDsm(String tableName, UberH3 h3) {
        this.tableName = tableName;
        this.h3 = h3;
    }

    public void save(Connection conn) {
        Map<Long, Integer> h3Map = h3.getH3Map();
        for (Entry<Long, Integer> entry : h3Map.entrySet()) {
            long h3Address = entry.getKey();
            int height = entry.getValue();
            isIntersectRoad(conn, h3Address);
        }
        System.out.printf("total : %d, real : %d\n", h3Map.size(), count);
    }

    private boolean isIntersectRoad(Connection conn, long h3Address) {
        List<LatLng> h3Boundary = h3.getH3Boundary(h3Address);
        byte[] bytes = wkb.makeH3BoundaryToPolygon(h3Boundary);
        String sql = createQuery();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, bytes);
            if (showResult(ps)) {
                count += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String createQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT count(*) from ");
        query.append("road_segment ");
        query.append("WHERE ST_intersects(st_setSRID(? ::geometry, 4326), the_geom) ");
        query.append("group by sig_cd");
        return query.toString();
    }

    private boolean showResult(PreparedStatement ps) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
