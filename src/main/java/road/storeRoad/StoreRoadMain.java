package road.storeRoad;

import config.ApplicationProperties;
import road.storeRoad.service.StoreRoadService;

public class StoreRoadMain {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        StoreRoadService storeRoadService = new StoreRoadService();
        storeRoadService.run();
    }
}
