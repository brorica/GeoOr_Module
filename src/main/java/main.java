import config.ApplicationProperties;
import service.AdminSectorService;
import service.DsmService;
import service.RoadService;


public class main {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        AdminSectorService adminSectorService = new AdminSectorService();
        RoadService roadService = new RoadService();
        DsmService dsmService = new DsmService();
        adminSectorService.storeAdminSector();
        roadService.storeRoad();
        dsmService.storeDsm();
    }
}
