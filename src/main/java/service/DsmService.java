package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import repository.dsm.DsmRepository;
import repository.dsm.HexagonAdminRepository;
import repository.dsm.HexagonRepository;
import repository.dsm.HexagonRoadRepository;

public class DsmService implements Service {

    private final DsmRepository origin;
    private final HexagonRepository hexagonRepository;
    private final HexagonAdminRepository hexagonAdminRepository;
    private final HexagonRoadRepository hexagonRoadRepository;
    private final String originTableName = "dsm";

    public DsmService() {
        this.origin = new DsmRepository(originTableName);
        this.hexagonRepository = new HexagonRepository("hexagon");
        this.hexagonAdminRepository = new HexagonAdminRepository("hexagon_admin");
        this.hexagonRoadRepository = new HexagonRoadRepository("hexagon_road");
    }

    @Override
    public void save() {
        String path = getProperty("dsm");
        String extension = "xyz";
        //origin.run(getFiles(path, extension));
        try {
            hexagonRepository.run(getFiles(path, extension));
            hexagonAdminRepository.run();
            hexagonRoadRepository.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<File> getFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        return Arrays.asList(files);
    }
}
