package repository.dsm;

import domain.HillShadeGrid;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateDsmSigCode {

    private final int batchLimitValue = 4096;
    private final WKB wkb = new WKB();

    public void update(Connection conn, List<HillShadeGrid> hillShadeGrids) throws SQLException {
        String updateSql = createUpdateSql();
        try (PreparedStatement pStmt = conn.prepareStatement(updateSql)) {
            setPrepared(pStmt, hillShadeGrids);
        }
    }

    public void setPrepared(PreparedStatement pStmt, List<HillShadeGrid> hillShadeGrids) throws SQLException {
        int batchLimit = batchLimitValue, recordCount = 0;
        for (HillShadeGrid hillShadeGrid : hillShadeGrids) {
            pStmt.setBytes(1, wkb.convertPolygonWKB(hillShadeGrid));
            pStmt.setString(2, String.valueOf((int)hillShadeGrid.getDsmX()));
            pStmt.setString(3, String.valueOf((int)hillShadeGrid.getDsmY()));
            pStmt.addBatch();

            if(--batchLimit == 0) {
                recordCount += pStmt.executeBatch().length;
                batchLimit = batchLimitValue;
                System.out.printf("dsm sig_cd update ... %d\n", recordCount);
            }
        }
        recordCount += pStmt.executeBatch().length;
        System.out.printf("dsm sig_cd update ... %d\n", recordCount);
    }

    private String createUpdateSql() {
        return "UPDATE dsm"
            + " SET sig_cd = (SELECT sig_cd FROM road WHERE ST_Overlaps(?, geom) LIMIT 1)"
            + " WHERE x LIKE ?"
            + " AND y LIKE ?";
    }


}
