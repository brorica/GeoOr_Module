package repository.dsm;

import geoUtil.WKB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveDsmTemp {

    private final int batchCountLimit = 648000;
    private final WKB wKb = new WKB();

    public void save(Connection conn, File[] dsms) throws SQLException {
        String sql = "INSERT INTO dsm_temp (x, y, z, sig_cd) VALUES(?, ?, ?, (SELECT adm_sect_cd FROM admin_sector WHERE ST_intersects(st_setSRID(? ::geometry, 4326), the_geom) LIMIT 1))";
        long totalBatchCount = 0;
        long startTime, endTime;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (File dsm : dsms) {
                System.out.printf("save %s [", dsm.getName());
                startTime = System.currentTimeMillis();
                totalBatchCount += readDsmToPoint(ps, dsm);
                endTime = System.currentTimeMillis();
                System.out.println("cost : " + (endTime - startTime) / 1000 + "s");
            }
        } catch (SQLException | IOException e) {
            conn.rollback();
            e.printStackTrace();
        }
        System.out.printf("\ntotalBatchCount : %d\n", totalBatchCount);
    }

    private int readDsmToPoint(PreparedStatement ps, File dsm) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(dsm));
        int batchCount = batchCountLimit, batchResult = 0;
        double s0, s1, s2;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] s = line.split(" ");
            s0 = Double.parseDouble(s[0]);
            s1 = Double.parseDouble(s[1]);
            s2 = Double.parseDouble(s[2]);

            ps.setDouble(1, s0);
            ps.setDouble(2, s1);
            ps.setDouble(3, s2);
            ps.setBytes(4, wKb.convertPointWKB(s[0], s[1]));
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

