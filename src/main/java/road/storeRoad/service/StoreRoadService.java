package road.storeRoad.service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import road.storeRoad.domain.Shp;
import road.storeRoad.repository.StoreRoadRepository;
import util.CreateSqlReader;

public class StoreRoadService {

    private final StoreRoadRepository storeRoadRepository;

    public StoreRoadService() {
        storeRoadRepository = new StoreRoadRepository();
    }

    public void run() {
        storeRoadRepository.createTable(getSqlReader());
        storeRoadRepository.save(getShps());
    }

    private CreateSqlReader getSqlReader() {
        File file = new File(getProperty("road.createFilePath"));
        return new CreateSqlReader(file);
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
