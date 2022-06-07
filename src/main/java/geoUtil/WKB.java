package geoUtil;

import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
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

        /**
         * Coordinate 매개변수는 x, y로 돼서 long, lat 으로 넣어야 할 거 같지만,
         * lat, long 순으로 넣어야 한다.
         * https://stackoverflow.com/questions/54362007/geotools-library-how-to-convert-korean-coordinates-epsg5179-to-decimal-degr
         */
        Coordinate coord1 = new Coordinate(latitude_1, longitude_1);
        Coordinate coord2 = new Coordinate(latitude_2, longitude_1);
        Coordinate coord3 = new Coordinate(latitude_2, longitude_2);
        Coordinate coord4 = new Coordinate(latitude_1, longitude_2);

        Coordinate[] coords =
            new Coordinate[]{coord1, coord2, coord3, coord4, coord1};
        LinearRing ring = geometryFactory.createLinearRing(coords);
        LinearRing holes[] = null;
        Polygon polygon = geometryFactory.createPolygon(ring, holes);
        return wkbWriter.write(srid.convertGeometry(polygon));
    }

    public byte[] convertPointWKB(List<Double> coordinates) {
        double latitude = coordinates.get(0);
        double longitude = coordinates.get(1);
        Coordinate coord = new Coordinate(latitude, longitude);
        Point point = geometryFactory.createPoint(coord);
        return wkbWriter.write(srid.convertGeometry(point));
    }
}
