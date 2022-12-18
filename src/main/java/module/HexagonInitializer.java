package module;

import config.ApplicationProperties;
import service.HexagonService;

public class HexagonInitializer {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        HexagonService hexagonService = new HexagonService();
        hexagonService.save();
    }
}
