package repository.hexagon;

import domain.Hexagon;
import domain.HexagonMap;
import geoUtil.UberH3;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

public class SaveHexagon {

    private final int batchLimitValue = 640000;
    private final WKB wkb = new WKB();
    private final UberH3 h3 = new UberH3();

    private final String tableName;

    public SaveHexagon(String tableName) {
        this.tableName = tableName;
    }

    public void save(Connection conn, HexagonMap hexagonMap) throws SQLException {
        String sql = createQuery();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            saveH3Map(ps,hexagonMap);
        } catch (SQLException e) {
            conn.commit();
            e.printStackTrace();
        }
    }

    private void saveH3Map(PreparedStatement ps, HexagonMap hexagonMap) throws SQLException {
        int batchCount = batchLimitValue;
        Map<Long, Hexagon> h3Map = hexagonMap.getH3Map();
        for (Entry<Long, Hexagon> entry : h3Map.entrySet()) {
            long h3Address = entry.getKey();
            Hexagon hexagon = entry.getValue();
            byte[] bytes = wkb.makeH3BoundaryToPolygon(h3.getH3Boundary(h3Address));
            ps.setBytes(1, bytes);
            ps.setLong(2, h3Address);
            ps.setInt(3, hexagon.getAverageHeight());
            ps.addBatch();
            if (--batchCount == 0) {
                ps.executeBatch();
                batchCount = batchLimitValue;
            }
        }
        ps.executeBatch();
    }

    private String createQuery() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" VALUES(?, ?, ?)");
        return query.toString();
    }
}
