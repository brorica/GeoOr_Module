import config.ApplicationProperties;
import service.AdminSectorService;
import service.DsmService;
import service.FrozenService;
import service.RoadService;


public class Main {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        AdminSectorService adminSectorService = new AdminSectorService();
        RoadService roadService = new RoadService();
        DsmService dsmService = new DsmService();
        FrozenService frozenService = new FrozenService();

        adminSectorService.save();
        roadService.save();
        dsmService.save();
        frozenService.save();
    }
}
