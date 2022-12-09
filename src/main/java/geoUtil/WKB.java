package geoUtil;

import com.uber.h3core.util.LatLng;
import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBWriter;

;

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

    // 한국 좌표가 이상한게 위경도 값을 바꿔 넣어줘야 한다.
    public byte[] makeH3BoundaryToPolygon(List<LatLng> latLngs) {
        Coordinate[] coords = new Coordinate[7];
        LatLng latLng = latLngs.get(0);
        // Polygon의 마지막 좌표는 시작점이어야 함.
        coords[6] = new Coordinate(latLng.lng, latLng.lat);
        for (int i = 0; i < 6; i++) {
            latLng = latLngs.get(i);
            coords[i] = new Coordinate(latLng.lng, latLng.lat);
        }
        LinearRing ring = geometryFactory.createLinearRing( coords );
        LinearRing holes[] = null;
        return wkbWriter.write(geometryFactory.createPolygon(ring, holes));
    }
}
