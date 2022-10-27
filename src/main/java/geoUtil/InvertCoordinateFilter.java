package geoUtil;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;

/**
 * 좌표계 변환 할 때, lat, lon을 서로 바꿔서 변환해야 할 때가 있다. 그럴 때 (Geometry 타입 변수명).apply(new
 * InvertCoordinateFilter()) 을 해주자.
 */

public class InvertCoordinateFilter implements CoordinateFilter {

    public void filter(Coordinate coord) {
        double oldX = coord.x;
        coord.x = coord.y;
        coord.y = oldX;
    }
}
