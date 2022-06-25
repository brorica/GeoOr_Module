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
    private final SRID5181 srid5181 = new SRID5181();
    private final SRID5179 srid5179 = new SRID5179();

    public byte[] convertPolygonWKB(List<Double> coordinates) {
        double latitude_1 = coordinates.get(0);
        double longitude_1 = coordinates.get(1);
        double latitude_2 = coordinates.get(2);
        double longitude_2 = coordinates.get(3);

        Coordinate coord1 = new Coordinate(longitude_1, latitude_1);
        Coordinate coord2 = new Coordinate(longitude_1, latitude_2);
        Coordinate coord3 = new Coordinate(longitude_2, latitude_2);
        Coordinate coord4 = new Coordinate(longitude_2, latitude_1);

        Coordinate[] coords =
            new Coordinate[]{coord1, coord2, coord3, coord4, coord1};
        LinearRing ring = geometryFactory.createLinearRing(coords);
        LinearRing holes[] = null;
        Polygon polygon = geometryFactory.createPolygon(ring, holes);
        return wkbWriter.write(polygon);
    }

    public byte[] convertPointWKB(String lat, String lon) {
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        Coordinate coord = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coord);
        return wkbWriter.write(point);
    }


    // 도로 polygon 변환용
    public byte[] convert5181To4326(Geometry geom) {
        Geometry geometry = srid5181.convertGeometry(geom);
        return wkbWriter.write(geometry);
    }
    // 지역 경계면 polygon 변환용
    public byte[] convert5179To4326(Geometry geom) {
        Geometry geometry = srid5179.convertGeometry(geom);
        return wkbWriter.write(geometry);
    }

    public byte[] convertWKB(Geometry geom) {
        return wkbWriter.write(geom);
    }
}
