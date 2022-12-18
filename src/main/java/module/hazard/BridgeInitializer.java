package module.hazard;

import config.ApplicationProperties;
import service.hazard.BridgeService;

public class BridgeInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        BridgeService BridgeService = new BridgeService();
        BridgeService.save();
    }
}
