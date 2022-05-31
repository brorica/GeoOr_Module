package storeDsm;

import config.ApplicationProperties;
import storeDsm.service.StoreDsmService;

public class StoreDsmMain {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        StoreDsmService storeDsmService = new StoreDsmService();
        storeDsmService.run();
    }

}
