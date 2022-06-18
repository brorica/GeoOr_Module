package repository.dsm;

import geoUtil.TransformCoordinate;
import geoUtil.WKB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 이 메소드는 dsm을 넣으면서 행정 구역도 찾아서 넣는 클래스다.
 * 매우 오래 걸린다.
 */
public class SaveDsmOverlpas {

    private final int batchCountLimit = 648000;
    private final WKB wKb = new WKB();
    private final TransformCoordinate transformCoordinate = new TransformCoordinate();

    public void save(Connection conn, File[] dsms) throws SQLException {
        String sql = "INSERT INTO dsm VALUES(?, ?, ?, (SELECT sig_cd FROM road_centroid WHERE ST_intersects(?, centroid) LIMIT 1))";
        long totalBatchCount = 0;
        long startTime, endTime;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (File dsm : dsms) {
                System.out.printf("save %s [", dsm.getName());
                startTime = System.currentTimeMillis();
                totalBatchCount += readDsmToGrid(ps, dsm);
                endTime = System.currentTimeMillis();
                System.out.println("cost : " + (endTime - startTime) / 1000 + "s");
            }
        } catch (SQLException | IOException e) {
            conn.rollback();
            e.printStackTrace();
        }
        System.out.printf("\ntotalBatchCount : %d\n", totalBatchCount);
    }

    private int readDsmToGrid(PreparedStatement ps, File dsm) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(dsm));
        int batchCount = batchCountLimit, batchResult = 0;
        double s0, s1, s2;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] s = line.split(" ");
            s0 = Double.parseDouble(s[0]);
            s1 = Double.parseDouble(s[1]);
            s2 = Double.parseDouble(s[2]);

            transformCoordinate.setXY(s0, s1);
            ps.setDouble(1, s0);
            ps.setDouble(2, s1);
            ps.setDouble(3, s2);
            ps.setBytes(4, wKb.convertPolygonWKB(transformCoordinate.createGridCoordinates()));
            ps.addBatch();

            if(--batchCount == 0) {
                batchResult += ps.executeBatch().length;
                batchCount = batchCountLimit;
                System.out.print('#');
                ps.clearBatch();
            }
        }

        batchResult += ps.executeBatch().length;
        ps.clearBatch();
        reader.close();
        System.out.printf("] %d records\n", batchResult);
        return batchResult;
    }
}
