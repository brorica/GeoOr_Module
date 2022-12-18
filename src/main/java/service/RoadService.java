package service;

import static config.ApplicationProperties.getProperty;

import domain.Shp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import repository.road.SegmentRoadRepository;
import repository.road.RoadRepository;

public class RoadService {

    private final RoadRepository origin;
    private final SegmentRoadRepository segment;

    private final String originTableName = "road";
    private final String segmentTableName = "road_segment";

    public RoadService() {
        this.origin = new RoadRepository(originTableName);
        this.segment = new SegmentRoadRepository(originTableName, segmentTableName);
    }

    public void save() {
        origin.run(getShps());
        segment.run();
    }

    private List<Shp> getShps() {
        String path = getProperty("road");
        String extension = "shp";
        List<File> shpFiles = getFiles(path, extension);

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

    public List<File> getFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        return Arrays.asList(files);
    }
}
