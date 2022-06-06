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
import java.util.ArrayList;

public class SaveDsm {

    private final int batchCountLimit = 4096;
    private final WKB wKb = new WKB();
    private final TransformCoordinate transformCoordinate = new TransformCoordinate();

    public void save(Connection conn, File[] dsms) throws SQLException {
        String sql = "INSERT INTO dsm VALUES(?, ?, ?, (SELECT sig_cd FROM road WHERE ST_Overlaps(?, geom) LIMIT 1))";
        long totalBatchCount = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (File dsm : dsms) {
                System.out.printf("save %s ... ", dsm.getName());
                totalBatchCount += readDsm(ps, dsm);
            }
        } catch (SQLException | IOException e) {
            // DSM 파일의 하나라도 오류가 나면 해당 파일 전체 작업 rollback.
            System.err.printf("오류, 다음 %d 개의 작업 rollback\n", totalBatchCount);
            conn.rollback();
            e.printStackTrace();
        }
        System.out.printf("\ntotalBatchCount : %d\n", totalBatchCount);
    }

    private int readDsm(PreparedStatement ps, File dsm) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(dsm));
        int batchCount = batchCountLimit, batchResult = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] s = line.split(" ");
            transformCoordinate.setXY(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
            ArrayList<Double> coordinates = transformCoordinate.createCoordinates();
            ps.setString(1, s[0]);
            ps.setString(2, s[1]);
            ps.setString(3, s[2]);
            ps.setBytes(4, wKb.convertPolygonWKB(coordinates));
            ps.addBatch();
            ps.clearParameters();

            if(--batchCount == 0) {
                batchResult += ps.executeBatch().length;
                batchCount = batchCountLimit;
                ps.clearBatch();
                System.out.println(batchResult);
            }
            System.out.println(batchCount);
        }

        batchResult += ps.executeBatch().length;
        ps.clearBatch();
        reader.close();
        System.out.printf("%d records\n", batchResult);
        return batchResult;
    }

}
