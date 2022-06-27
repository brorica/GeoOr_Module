package geoUtil;
;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBWriter;

public class WKB {

    private final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
    private final WKBWriter wkbWriter = new WKBWriter();
    private final SRID5181 srid5181 = new SRID5181();
    private final SRID5179 srid5179 = new SRID5179();

    public byte[] convertPointWKB(String lat, String lon) {
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        Coordinate coord = new Coordinate(latitude, longitude);
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
}
