package repository.dsm;

import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertH3 {

    private final H3Core h3;
    private final int h3Res = 9;
    private final Map<Long, Integer> h3Map = new HashMap<>();

    public ConvertH3(H3Core h3) throws IOException {
        this.h3 = H3Core.newInstance();
    }

    public void addH3Address(double lat, double lon, int height) {
        long h3LongAddress = h3.latLngToCell(lat, lon, h3Res);
        if (h3Map.containsKey(h3LongAddress)) {
            if (!isMaxHeight(h3LongAddress))
                return;
        }
        h3Map.put(h3LongAddress, height);
    }

    private boolean isMaxHeight(long h3LongAddress) {
        if (h3Map.get(h3LongAddress) > h3Res) {
            return false;
        }
        return true;
    }

    public List<LatLng> makeHexagon(long h3LongAddress) {
        return h3.cellToBoundary(h3LongAddress);
    }
}
