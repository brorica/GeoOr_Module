package repository.dsm;

import domain.Hexagon;
import geoUtil.UberH3;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

public class SaveHexagon {

    private final int batchLimitValue = 45000;
    private final WKB wkb = new WKB();
    private final String tableName;
    private final UberH3 h3;

    public SaveHexagon(String tableName, UberH3 h3) {
        this.tableName = tableName;
        this.h3 = h3;
    }

    public void save(Connection conn) throws SQLException {
        String sql = createQuery();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            saveH3Map(ps);
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    private void saveH3Map(PreparedStatement ps) throws SQLException {
        int batchCount = batchLimitValue;
        Map<Long, Hexagon> h3Map = h3.getH3Map();
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
