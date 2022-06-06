package service;

import java.util.ArrayList;

public class HillShadeService {

    public ArrayList<Double> getCoordinates(double x, double y) {
        ArrayList<Double> grid = new ArrayList<>();

        TransformCoordinate transformCoordinate = new TransformCoordinate(x + 1, y - 1);
        grid.add(transformCoordinate.latitude); grid.add(transformCoordinate.longitude);

        transformCoordinate = new TransformCoordinate(x - 1, y + 1);
        grid.add(transformCoordinate.latitude); grid.add(transformCoordinate.longitude);

        return grid;
    }

}
