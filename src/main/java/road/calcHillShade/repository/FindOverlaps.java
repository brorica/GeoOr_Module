package road.calcHillShade.repository;

import road.calcHillShade.domain.HillShadeGrid;
import road.calcHillShade.domain.Road;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.io.WKBWriter;

public class FindOverlaps {

    private final String sigCode;
    private final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    private final WKBWriter wkbWriter = new WKBWriter();

    public FindOverlaps(String sigCode) {
        this.sigCode = sigCode;
    }

    public void match(Connection conn, int baseIndex, List<Road> roads,
        List<HillShadeGrid> hillShadeGrids) throws SQLException {
        String selectSql = createSelectSql();
        try(PreparedStatement pStmt = conn.prepareStatement(selectSql)) {
            for (HillShadeGrid hillShadeGrid : hillShadeGrids) {
                pStmt.setString(1, sigCode + "%");
                pStmt.setBytes(2, convertPolygonWKB(hillShadeGrid));
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

    private byte[] convertPolygonWKB(HillShadeGrid hillShadeGrid) {
        double x1 = hillShadeGrid.getX1();
        double y1 = hillShadeGrid.getY1();
        double x2 = hillShadeGrid.getX2();
        double y2 = hillShadeGrid.getY2();

        Coordinate[] coords  =
            new Coordinate[] {new Coordinate(x1, y1), new Coordinate(x1, y2),
                new Coordinate(x2, y2), new Coordinate(x2, y1), new Coordinate(x1, y1) };
        LinearRing ring = geometryFactory.createLinearRing( coords );
        LinearRing holes[] = null;

        return wkbWriter.write(geometryFactory.createPolygon(ring, holes));
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
