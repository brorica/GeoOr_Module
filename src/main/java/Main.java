import config.ApplicationProperties;
import service.AdminSectorService;
import service.BridgeService;
import service.DsmService;
import service.FrozenService;
import service.RoadService;
import service.TunnelService;


public class Main {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        AdminSectorService adminSectorService = new AdminSectorService();
        RoadService roadService = new RoadService();
        DsmService dsmService = new DsmService();
        FrozenService frozenService = new FrozenService();
        TunnelService tunnelService = new TunnelService();
        BridgeService bridgeService = new BridgeService();

        adminSectorService.save();
        roadService.save();
        dsmService.save();
        frozenService.save();
        tunnelService.save();
        bridgeService.save();
    }
}
