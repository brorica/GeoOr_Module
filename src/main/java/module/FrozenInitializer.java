package module;

import config.ApplicationProperties;
import service.FrozenService;

public class FrozenInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        FrozenService frozenService = new FrozenService();
        frozenService.storeFrozen();
    }

}
