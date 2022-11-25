package module;

import config.ApplicationProperties;
import service.DsmService;

public class DsmInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        DsmService dsmService = new DsmService();
        dsmService.save();
    }
}
