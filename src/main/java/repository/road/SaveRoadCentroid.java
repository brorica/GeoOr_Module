package repository.road;

import domain.Shp;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;

public class SaveRoadCentroid {

    private final int batchLimitValue;
    private final WKB wkb;

    public SaveRoadCentroid() {
        this.batchLimitValue = 1024;
        this.wkb = new WKB();
    }

    public void save(Connection conn, List<Shp> shps) {
        String insertQuery = createQuery();
        int totalRecordCount = 0;
        try (PreparedStatement pStmt = conn.prepareStatement(insertQuery)) {
            for (Shp shp : shps) {
                System.out.printf("road_centroid table %s save start ... ", shp.getName());
                totalRecordCount += SetPreparedStatement(pStmt, shp);
                shp.close();
            }
            conn.commit();
            System.out.printf("total save : %s\n", totalRecordCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int SetPreparedStatement(PreparedStatement pStmt, Shp shp) throws SQLException {
        FeatureIterator<SimpleFeature> features = shp.getFeature();
        int batchLimit = batchLimitValue, recordCount = 0;
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            Point centroid = ((Geometry) feature.getDefaultGeometry()).getCentroid();
            pStmt.setBytes(1, wkb.convert5181To4326(centroid));
            pStmt.setObject(2, feature.getAttribute("SIG_CD"));
            pStmt.addBatch();
            if(--batchLimit == 0) {
                recordCount += pStmt.executeBatch().length;
                batchLimit = batchLimitValue;
            }
        }
        recordCount += pStmt.executeBatch().length;
        System.out.printf("%d save\n", recordCount);
        return recordCount;
    }


    private String createQuery() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO public.");
        query.append("road_centroid");
        query.append("(centroid, sig_cd) ");
        query.append(" VALUES (ST_FlipCoordinates(?), ?);");
        return query.toString();
    }
}
