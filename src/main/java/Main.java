import config.ApplicationProperties;
import repository.RefineRepository;
import service.AdminSectorService;
import service.hazard.BridgeService;
import service.HexagonService;
import service.hazard.FrozenService;
import service.RoadService;
import service.hazard.TunnelService;


public class Main {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        AdminSectorService adminSectorService = new AdminSectorService();
        RoadService roadService = new RoadService();
        HexagonService hexagonService = new HexagonService();
        FrozenService frozenService = new FrozenService();
        TunnelService tunnelService = new TunnelService();
        BridgeService bridgeService = new BridgeService();
        RefineRepository refineRepository = new RefineRepository();

        adminSectorService.save();
        roadService.save();
        hexagonService.save();
        frozenService.save();
        tunnelService.save();
        bridgeService.save();
        refineRepository.run();
    }
}
