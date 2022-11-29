package repository.dsm;

import com.uber.h3core.util.LatLng;
import domain.Hexagon;
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

    private final int batchLimitValue = 45000;
    private int batchCount = batchLimitValue;
    private final WKB wkb = new WKB();
    private final String tableName;
    private final UberH3 h3;

    public TestSaveDsm(String tableName, UberH3 h3) {
        this.tableName = tableName;
        this.h3 = h3;
    }

    public void save(Connection conn) {
        Map<Long, Hexagon> h3Map = h3.getH3Map();
        System.out.printf("save ");
        for (Entry<Long, Hexagon> entry : h3Map.entrySet()) {
            long h3Address = entry.getKey();
            Hexagon hexagon = entry.getValue();

            List<LatLng> h3Boundary = h3.getH3Boundary(h3Address);
            byte[] polygonWKB = wkb.makeH3BoundaryToPolygon(h3Boundary);
            List<Integer> sig_cds = findIntersectSigCd(conn, polygonWKB);
            saveIntersectSigCd(conn, sig_cds, polygonWKB, h3Address, hexagon.getAverageHeight());
        }
    }

    private List<Integer> findIntersectSigCd(Connection conn, byte[] polygonWKB) {
        String sql = createQuery1();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, polygonWKB);
            return showResult(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createQuery1() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT adm_sect_cd from ");
        query.append("admin_sector_segment ");
        query.append("WHERE ST_intersects(st_setSRID(? ::geometry, 4326), the_geom) ");
        query.append("group by adm_sect_cd");
        return query.toString();
    }

    private List<Integer> showResult(PreparedStatement ps) {
        List<Integer> sig_cds = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                sig_cds.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sig_cds;
    }

    private void saveIntersectSigCd(Connection conn, List<Integer> sig_cds, byte[] polygonWKB, long h3Address, int height) {
        String sql = createQuery2();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Integer sig_cd : sig_cds) {
                ps.setBytes(1, polygonWKB);
                ps.setLong(2, h3Address);
                ps.setInt(3, height);
                ps.setInt(4, sig_cd);
                ps.addBatch();

                if (--batchCount == 0) {
                    ps.executeBatch();
                    batchCount = batchLimitValue;
                    ps.clearBatch();
                }
            }
            ps.executeBatch();
            ps.clearBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String createQuery2() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" VALUES(?, ?, ?, ?)");
        return query.toString();
    }
}
