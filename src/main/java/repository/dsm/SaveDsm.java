package repository.dsm;

import geoUtil.WKB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import repository.RelateAdminSector;

/**
 * 대량의 dsm 파일을 넣는 과정에서 콘솔창에 에러 글이 뜨는 경우가 있는데
 * 저장되는데는 문제가 없으니 그냥 넘어가도 된다.
 */
public class SaveDsm extends RelateAdminSector {

    private final int batchLimitValue = 648000;
    private final WKB wkb = new WKB();

    private final String tableName;

    public SaveDsm(String tableName) {
        this.tableName = tableName;
    }

    public void save(Connection conn, File[] dsms) throws SQLException {
        String sql = getSQL();
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
        int batchCount = batchLimitValue, batchResult = 0;
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
            ps.setBytes(4, wkb.convertPointWKB(s[0], s[1]));
            ps.addBatch();

            if (--batchCount == 0) {
                batchResult += ps.executeBatch().length;
                batchCount = batchLimitValue;
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

    private String getSQL() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" (x, y, z, sig_cd) VALUES(?, ?, ?, (SELECT adm_sect_cd FROM ");
        query.append(getAdminSectorSegmentTableName());
        query.append(" WHERE ST_intersects(st_setSRID(? ::geometry, 4326), the_geom) LIMIT 1))");
        return query.toString();
    }
}

