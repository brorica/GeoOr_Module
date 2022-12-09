package repository.tunnel;

import domain.Shp;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import repository.Save;

public class SaveTunnel implements Save<Shp> {

    private final int batchLimitValue = 1024;
    private final WKB wkb = new WKB();
    private final String tableName;

    public SaveTunnel(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void save(Connection conn, List<Shp> shps) throws SQLException {
        String insertQuery = createQuery();
        int totalRecordCount = 0;
        try (PreparedStatement pStmt = conn.prepareStatement(insertQuery)) {
            for (Shp shp : shps) {
                System.out.printf("tunnel table %s save start ... ", shp.getName());
                totalRecordCount += SetPreparedStatement(pStmt, shp);
                shp.close();
            }
            conn.commit();
            System.out.printf("total save : %s\n", totalRecordCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String createQuery() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO public.");
        query.append(tableName);
        query.append(" VALUES (ST_FlipCoordinates(?), ?, ?, ");
        query.append("(SELECT sig_cd FROM ");
        query.append("admin_sector_segment");
        query.append(
            " WHERE ST_intersects(st_setSRID(ST_FlipCoordinates(?) ::geometry, 4326), the_geom) LIMIT 1))");
        return query.toString();
    }

    private int SetPreparedStatement(PreparedStatement pStmt, Shp shp) throws SQLException {
        FeatureIterator<SimpleFeature> features = shp.getFeature();
        int batchLimit = batchLimitValue, recordCount = 0;
        while (features.hasNext()) {
            SimpleFeature feature = features.next();

            byte[] centerPoint = getCentroid((Geometry) feature.getAttribute(0));
            pStmt.setBytes(1,centerPoint);
            pStmt.setObject(2, feature.getAttribute("UFID"));
            pStmt.setObject(3, feature.getAttribute("NAME"));
            pStmt.setBytes(4, centerPoint);

            pStmt.addBatch();
            if (--batchLimit == 0) {
                recordCount += pStmt.executeBatch().length;
                batchLimit = batchLimitValue;
            }
        }
        recordCount += pStmt.executeBatch().length;
        System.out.printf("%d save\n", recordCount);
        return recordCount;
    }

    private byte[] getCentroid(Geometry Geometry) {
        return wkb.convert5179To4326(Geometry.getCentroid());
    }
}
