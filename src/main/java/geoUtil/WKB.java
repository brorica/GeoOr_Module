package geoUtil;

import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.io.WKBWriter;

public class WKB {

    private final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    private final WKBWriter wkbWriter = new WKBWriter();
    private final SRID srid = new SRID();

    public byte[] convertPolygonWKB(List<Double> coordinates) {
        double latitude_1 = coordinates.get(0);
        double longitude_1 = coordinates.get(1);
        double latitude_2 = coordinates.get(2);
        double longitude_2 = coordinates.get(3);

        Coordinate coord1 = srid.convertPoint(longitude_1, latitude_1);
        Coordinate coord2 = srid.convertPoint(longitude_1, latitude_2);
        Coordinate coord3 = srid.convertPoint(longitude_2, latitude_2);
        Coordinate coord4 = srid.convertPoint(longitude_2, latitude_1);

        Coordinate[] coords =
            new Coordinate[]{coord1, coord2, coord3, coord4, coord1};
        LinearRing ring = geometryFactory.createLinearRing(coords);
        LinearRing holes[] = null;

        return wkbWriter.write(geometryFactory.createPolygon(ring, holes));
    }
}
