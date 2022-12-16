package module.hazard;

import config.ApplicationProperties;
import service.hazard.TunnelService;

public class TunnelInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        TunnelService tunnelService = new TunnelService();
        tunnelService.save();
    }
}
