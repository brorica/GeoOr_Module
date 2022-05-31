package road.calcHillShade;

import road.calcHillShade.service.RoadHillShadeService;

public class main {

    private static String[] sigCode  = {
        "11", "26", "27", "28",
        "29", "30", "31", "36", "41",
        "42", "43", "44", "45", "46",
        "47", "48", "50"
    };

    public static void main(String[] args) {
        RoadHillShadeService service = new RoadHillShadeService();
        service.start("36", null);
    }
}
