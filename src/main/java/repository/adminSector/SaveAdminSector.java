package repository.adminSector;

import domain.Shp;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import repository.Save;

public class SaveAdminSector implements Save<Shp> {

    private final int batchLimitValue = 1024;
    private final WKB wkb = new WKB();
    private final String tableName;

    public SaveAdminSector(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void save(Connection conn, List<Shp> shps) {
        String insertQuery = createQuery();
        int totalRecordCount = 0;
        try (PreparedStatement pStmt = conn.prepareStatement(insertQuery)) {
            for (Shp shp : shps) {
                System.out.printf("%s save start ... ", shp.getName());
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
        query.append(" VALUES (ST_FlipCoordinates(?), ?)");
        return query.toString();
    }

    private int SetPreparedStatement(PreparedStatement pStmt, Shp shp) throws SQLException {
        FeatureIterator<SimpleFeature> features = shp.getFeature();
        int batchLimit = batchLimitValue, recordCount = 0;
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            pStmt.setBytes(1,
                wkb.convert5179To4326((Geometry) feature.getDefaultGeometryProperty().getValue()));
            pStmt.setInt(2, Integer.parseInt((String) feature.getAttribute("ADM_SECT_C")));
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
