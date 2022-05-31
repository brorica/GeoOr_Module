package road.storeRoad.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import road.storeRoad.domain.Shp;

public class SaveRoad {

    private final int batchLimitValue;
    private final WKBWriter writer;

    public SaveRoad() {
        this.batchLimitValue = 1024;
        this.writer = new WKBWriter();
    }

    public void save(Connection conn, List<Shp> shps) {
        String insertQuery = createQuery(shps.get(0));
        int totalRecordCount = 0;
        try (PreparedStatement pStmt = conn.prepareStatement(insertQuery)) {
            for (Shp shp : shps) {
                System.out.printf("table %s save start ... ", shp.getName());
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
        List<AttributeDescriptor> attributeNames = shp.getAttributeNames();
        int batchLimit = batchLimitValue, recordCount = 0;
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            pStmt.setObject(1, writer.write((Geometry) feature.getDefaultGeometryProperty().getValue()));
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

    private String createQuery(Shp shp) {
        int attributesCount = shp.getAttributeNames().size();
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO public.");
        query.append("road");
        query.append("(geom, opert_de, rw_sn, sig_cd) ");
        query.append(" VALUES (");
        for (int i = 0; i < attributesCount - 1; i++) {
            query.append("?, ");
        }
        query.append("?);");
        System.out.println(query);
        return query.toString();
    }
}
