package geoUtil;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import java.io.IOException;
import java.util.List;

public class UberH3 {

    private H3Core h3;
    private final int h3Res = 10;

    public UberH3() {
        try {
            this.h3 = H3Core.newInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getLatLngToCell(double lat, double lon) {
        return h3.latLngToCell(lon, lat, h3Res);
    }

    public List<LatLng> getH3Boundary(long h3Address) {
        return h3.cellToBoundary(h3Address);
    }
}
