package repository.dsm;

import com.uber.h3core.util.LatLng;
import geoUtil.UberH3;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestSaveDsm {

    private final int batchLimitValue = 648000;
    private final WKB wkb = new WKB();
    private final String tableName;
    private final UberH3 h3;
    private final List<Integer> sig_cds = new ArrayList<>();

    public TestSaveDsm(String tableName, UberH3 h3) {
        this.tableName = tableName;
        this.h3 = h3;
    }

    public void save(Connection conn) {
        long totalBatchCount = 0;
        Map<Long, Integer> h3Map = h3.getH3Map();
        for (Entry<Long, Integer> entry : h3Map.entrySet()) {
            long h3Address = entry.getKey();
            int height = entry.getValue();
            sig_cds.clear();
            byte[] polygonWKB = findIntersectSigCd(conn, h3Address);
            totalBatchCount += saveIntersectSigCd(conn, polygonWKB, h3Address, height);
        }
        System.out.println("totalBatchCount = " + totalBatchCount);
    }

    private byte[] findIntersectSigCd(Connection conn, long h3Address) {
        List<LatLng> h3Boundary = h3.getH3Boundary(h3Address);
        byte[] bytes = wkb.makeH3BoundaryToPolygon(h3Boundary);
        String sql = createQuery1();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, bytes);
            showResult(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private String createQuery1() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT sig_cd from ");
        query.append("road_segment ");
        query.append("WHERE ST_intersects(st_setSRID(? ::geometry, 4326), the_geom) ");
        query.append("group by sig_cd");
        return query.toString();
    }

    private void showResult(PreparedStatement ps) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                sig_cds.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int saveIntersectSigCd(Connection conn, byte[] polygonWKB, long h3Address, int height) {
        String sql = createQuery2();
        int batchCount = batchLimitValue, batchResult = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Integer sig_cd : sig_cds) {
                ps.setBytes(1, polygonWKB);
                ps.setLong(2, h3Address);
                ps.setInt(3, height);
                ps.setInt(4, sig_cd);
                ps.addBatch();

                if (--batchCount == 0) {
                    batchResult += ps.executeBatch().length;
                    batchCount = batchLimitValue;
                    ps.clearBatch();
                }
            }
            batchResult += ps.executeBatch().length;
            ps.clearBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return batchResult;
    }

    private String createQuery2() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" VALUES(?, ?, ?, ?)");
        return query.toString();
    }
}
