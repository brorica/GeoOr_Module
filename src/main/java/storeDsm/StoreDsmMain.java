package storeDsm;

import config.ApplicationProperties;
import storeDsm.service.DsmService;

public class StoreDsmMain {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        DsmService dsmService = new DsmService();
        dsmService.run();
    }

}
