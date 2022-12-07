package domain;

import java.util.HashMap;
import java.util.Map;

public class HexagonMap {

    private final Map<Long, Hexagon> h3Map = new HashMap<>();

    public void addCellIndex(long cellIndex, int height) {
        if (h3Map.containsKey(cellIndex)) {
            Hexagon hexagon = h3Map.get(cellIndex);
            hexagon.addDsmInfo(height);
        }
        h3Map.put(cellIndex, new Hexagon(height));
    }
}
