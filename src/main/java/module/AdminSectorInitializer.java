package module;

import config.ApplicationProperties;
import service.AdminSectorService;

public class AdminSectorInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        AdminSectorService adminSectorService = new AdminSectorService();
        adminSectorService.storeAdminSector();
    }
}
