package service;

import static config.ApplicationProperties.getProperty;

import domain.Shp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import repository.adminSector.AdminSectorRepository;
import repository.adminSector.SegmentAdminSectorRepository;

public class AdminSectorService {

    private final AdminSectorRepository origin;
    private final SegmentAdminSectorRepository segment;

    public AdminSectorService() {
        this.origin = new AdminSectorRepository();
        this.segment = new SegmentAdminSectorRepository();
    }

    public void storeAdminSector() {
        origin.run(getShps());
        segment.run();
    }

    private List<Shp> getShps() {
        List<Shp> shps = new ArrayList<>();
        File[] shpFiles = findShpFiles(getProperty("adminSector"));
        for (File file : shpFiles) {
            try {
                shps.add(new Shp(file));
            } catch (IOException e) {
                System.err.printf("%s 식별 과정에 오류가 발생했습니다.\n", file.getName());
                e.printStackTrace();
            }
        }
        return shps;
    }

    private File[] findShpFiles(String shpPath) {
        File directory = new File(shpPath);
        String extension = "shp";
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }
}
