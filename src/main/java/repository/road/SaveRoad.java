package repository.road;

import domain.Shp;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

public class SaveRoad {

    private final int batchLimitValue = 1024;
    private final WKB wkb = new WKB();

    private final String roadTable;

    public SaveRoad(String roadTable) {
        this.roadTable = roadTable;
    }

    public void save(Connection conn, List<Shp> shps) {
        String insertQuery = createQuery();
        int totalRecordCount = 0;
        try (PreparedStatement pStmt = conn.prepareStatement(insertQuery)) {
            for (Shp shp : shps) {
                System.out.printf("road table %s save start ... ", shp.getName());
                totalRecordCount += SetPreparedStatement(pStmt, shp);
                shp.close();
            }
            conn.commit();
            System.out.printf("total save : %s\n", totalRecordCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String createQuery() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO public.");
        query.append(roadTable);
        query.append("(the_geom, sig_cd) ");
        query.append(" VALUES (ST_FlipCoordinates(?), ?)");
        return query.toString();
    }

    private int SetPreparedStatement(PreparedStatement pStmt, Shp shp) throws SQLException {
        FeatureIterator<SimpleFeature> features = shp.getFeature();
        int batchLimit = batchLimitValue, recordCount = 0;
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            pStmt.setBytes(1,
                wkb.convert5181To4326((Geometry) feature.getDefaultGeometryProperty().getValue()));
            pStmt.setInt(2, Integer.parseInt((String) feature.getAttribute("SIG_CD")));

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
}
