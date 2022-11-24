package service;

import static config.ApplicationProperties.getProperty;

import domain.Shp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import repository.adminSector.AdminSectorRepository;
import repository.adminSector.SegmentAdminSectorRepository;

public class AdminSectorService implements Service {

    private final AdminSectorRepository origin;
    private final SegmentAdminSectorRepository segment;

    private final String originTableName = "road";
    private final String segmentTableName = "road_segment";


    public AdminSectorService() {
        this.origin = new AdminSectorRepository(originTableName);
        this.segment = new SegmentAdminSectorRepository(originTableName, segmentTableName);
    }

    @Override
    public void save() {
        origin.run(getShps());
        segment.run();
    }

    private List<Shp> getShps() {
        String path = getProperty("adminSector");
        String extension = "shp";
        File[] shpFiles = getFiles(path, extension);

        List<Shp> shps = new ArrayList<>();
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

    @Override
    public File[] getFiles(String path, String extension) {
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }
}
