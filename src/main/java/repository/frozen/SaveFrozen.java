package repository.frozen;

import geoUtil.WKB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import repository.RelateAdminSector;
import repository.Save;

public class SaveFrozen extends RelateAdminSector implements Save<File> {

    private final int batchLimitValue = 648000;
    private final WKB wkb = new WKB();
    private final String tableName;

    public SaveFrozen(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void save(Connection conn, List<File> files) throws SQLException {
        String sql = createQuery();
        long totalBatchCount = 0;
        System.out.print("save frozen data... ");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (File file : files) {
                totalBatchCount += readData(ps, file);
            }
            System.out.println(totalBatchCount);
            conn.commit();
        } catch (SQLException | IOException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public String createQuery() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" VALUES(?, (SELECT sig_cd FROM ");
        query.append(getAdminSectorSegmentTableName());
        query.append(" WHERE ST_intersects(st_setSRID(? ::geometry, 4326), the_geom) LIMIT 1))");
        return query.toString();
    }

    private int readData(PreparedStatement ps, File file) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int batchCount = batchLimitValue, batchResult = 0;
        String latitude, longitude;
        String line;
        while ((line = reader.readLine()) != null) {
            // 모든 공백 분리
            String[] s = line.split("\\s");
            // 한국 좌표계가 이상한게 위경도 데이터를 바꿔서 넣어줘야 geotools에서 올바르게 인식한다
            latitude = s[1];
            longitude = s[0];

            byte[] pointWKB = wkb.convertPointWKB(latitude, longitude);
            ps.setBytes(1, pointWKB);
            ps.setBytes(2, pointWKB);
            ps.addBatch();
            if (--batchCount == 0) {
                batchResult += ps.executeBatch().length;
                batchCount = batchLimitValue;
                ps.clearBatch();
            }
        }
        batchResult += ps.executeBatch().length;
        ps.clearBatch();
        reader.close();
        return batchResult;
    }
}
