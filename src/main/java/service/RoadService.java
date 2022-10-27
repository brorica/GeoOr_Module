package service;

import static config.ApplicationProperties.getProperty;

import domain.SqlReader;
import domain.Shp;
import java.io.File;
import repository.road.RoadRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoadService {

    private final RoadRepository roadRepository;

    public RoadService() {
        roadRepository = new RoadRepository();
    }

    public void storeRoad() {
        roadRepository.saveOriginData(getSqlReader(getProperty("road")), getShps());
        roadRepository.procOriginData(getSqlReader(getProperty("roadDivide")));
    }

    private SqlReader getSqlReader(String path) {
        File file = new File(path);
        return new SqlReader(file);
    }

    private List<Shp> getShps() {
        List<Shp> shps = new ArrayList<>();
        File[] shpFiles = findShpFiles(getProperty("road.shpPath"));
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
