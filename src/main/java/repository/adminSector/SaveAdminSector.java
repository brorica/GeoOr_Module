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

public class SaveAdminSector {

    private final int batchLimitValue;
    private final WKB wkb;

    public SaveAdminSector() {
        this.batchLimitValue = 1024;
        this.wkb = new WKB();
    }

    public void save(Connection conn, List<Shp> shps) {
        String insertQuery = createQuery();
        int totalRecordCount = 0;
        try (PreparedStatement pStmt = conn.prepareStatement(insertQuery)) {
            for (Shp shp : shps) {
                System.out.printf("road table %s save start ... ", shp.getName());
                totalRecordCount += SetPreparedStatement(pStmt, shp);
            }
            conn.commit();
            System.out.printf("total save : %s\n", totalRecordCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int SetPreparedStatement(PreparedStatement pStmt, Shp shp) throws SQLException {
        FeatureIterator<SimpleFeature> features = shp.getFeature();
        List<AttributeDescriptor> attributeNames = shp.getAttributeNames();
        int batchLimit = batchLimitValue, recordCount = 0;
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            pStmt.setBytes(1, wkb.convert5179To4326((Geometry) feature.getDefaultGeometryProperty().getValue()));
            for (int i = 1; i < attributeNames.size(); i++) {
                String name = attributeNames.get(i).getLocalName();
                pStmt.setObject(i + 1, feature.getAttribute(name));
            }
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
        query.append("admin_sector");
        query.append(" VALUES (?, ?, ?, ?, ?, ?);");
        return query.toString();
    }
}
