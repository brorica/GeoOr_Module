package module;

import config.ApplicationProperties;
import service.RoadService;

public class RoadInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        RoadService roadService = new RoadService();
        roadService.storeRoad();
    }

}
