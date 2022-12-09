package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import repository.hexagon.HexagonAdminRepository;
import repository.hexagon.HexagonRepository;
import repository.hexagon.HexagonRoadRepository;

public class DsmService implements Service {

    private final HexagonRepository hexagonRepository;
    private final HexagonAdminRepository hexagonAdminRepository;
    private final HexagonRoadRepository hexagonRoadRepository;

    public DsmService() {
        this.hexagonRepository = new HexagonRepository("hexagon");
        this.hexagonAdminRepository = new HexagonAdminRepository("hexagon_admin");
        this.hexagonRoadRepository = new HexagonRoadRepository("hexagon_road");
    }

    @Override
    public void save() {
        String path = getProperty("dsm");
        String extension = "xyz";
        try {
            hexagonRepository.run(getFiles(path, extension));
            hexagonAdminRepository.run();
            hexagonRoadRepository.run();
        } catch (Exception e) {
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
