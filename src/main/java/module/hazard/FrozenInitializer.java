package module.hazard;

import config.ApplicationProperties;
import service.hazard.FrozenService;

public class FrozenInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        FrozenService frozenService = new FrozenService();
        frozenService.save();
    }
}
