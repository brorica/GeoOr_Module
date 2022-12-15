package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import repository.hexagon.HexagonAdminRepository;
import repository.hexagon.HexagonRepository;
import repository.hexagon.HexagonRoadRepository;

public class HexagonService implements Service {

    private final HexagonRepository hexagonRepository;
    private final HexagonAdminRepository hexagonAdminRepository;
    private final HexagonRoadRepository hexagonRoadRepository;

    private final String originTableName = "hexagon";
    private final String hexagonAdminTableName = "hexagon_admin";
    private final String hexagonRoadTableName = "hexagon_road";

    public HexagonService() {
        this.hexagonRepository = new HexagonRepository("hexagon");
        this.hexagonAdminRepository = new HexagonAdminRepository(originTableName, hexagonAdminTableName);
        this.hexagonRoadRepository = new HexagonRoadRepository(originTableName, hexagonRoadTableName);
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
