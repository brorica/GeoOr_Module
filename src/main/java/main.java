import config.ApplicationProperties;
import service.HillShadeService;

import java.util.ArrayList;

public class main {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        HillShadeService hillShadeService = new HillShadeService();
        ArrayList<Double> coordinates = hillShadeService.getCoordinates(354211, 574118);
    }
}
