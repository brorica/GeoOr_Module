package repository.road;

import domain.HillShadeGrid;
import domain.Road;
import geoUtil.WKB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FindOverlaps {

    private final String sigCode;
    private final WKB wkb = new WKB();

    public FindOverlaps(String sigCode) {
        this.sigCode = sigCode;
    }

    public void match(Connection conn, int baseIndex, List<Road> roads,
        List<HillShadeGrid> hillShadeGrids) throws SQLException {
        String selectSql = createSelectSql();
        try(PreparedStatement pStmt = conn.prepareStatement(selectSql)) {
            for (HillShadeGrid hillShadeGrid : hillShadeGrids) {
                pStmt.setString(1, sigCode + "%");
                pStmt.setBytes(2, wkb.convertPolygonWKB(hillShadeGrid));
                calcHillShade(pStmt, baseIndex, roads, hillShadeGrid.getHillShade());
            }
        }
    }

    private String createSelectSql() {
        return ""
            + "SELECT id "
            + "from road "
            + "where sig_cd like ? "
            + "AND ST_Overlaps(?, geom)";
    }

    private void calcHillShade(PreparedStatement pStmt, int baseIndex, List<Road> roads, int hillShade) throws SQLException {
        int index;
        try (ResultSet rs = pStmt.executeQuery()) {
            while (rs.next()) {
                index = rs.getInt("id") - baseIndex;
                roads.get(index).matchGrid(hillShade);
            }
        }
    }
}
