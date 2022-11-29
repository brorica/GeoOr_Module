package geoUtil;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import domain.Hexagon;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UberH3 {

    private H3Core h3;
    private final int h3Res = 10;
    private final Map<Long, Hexagon> h3Map = new HashMap<>();

    public UberH3() {
        try {
            this.h3 = H3Core.newInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addH3Address(double lat, double lon, int height) {
        long h3Address = h3.latLngToCell(lon, lat, h3Res);
        if (h3Map.containsKey(h3Address)) {
            Hexagon hexagon = h3Map.get(h3Address);
            hexagon.addDsmInfo(height);
        }
        h3Map.put(h3Address, new Hexagon(height));
    }

    public LatLng getH3Centroid(long h3Address) {
        return h3.cellToLatLng(h3Address);
    }

    public List<LatLng> getH3Boundary(long h3Address) {
        return h3.cellToBoundary(h3Address);
    }

    public Map<Long, Hexagon> getH3Map() {
        return h3Map;
    }
}
