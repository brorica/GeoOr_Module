import config.ApplicationProperties;
import service.DsmService;
import service.RoadService;


public class main {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        RoadService roadService = new RoadService();
        DsmService dsmService = new DsmService();
        roadService.storeRoad();
        dsmService.storeDsm();
    }
}
