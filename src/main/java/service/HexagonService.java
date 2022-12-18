package service;

import static config.ApplicationProperties.getPath;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import repository.hexagon.HexagonAdminRepository;
import repository.hexagon.HexagonRepository;
import repository.hexagon.HexagonRoadRepository;

public class HexagonService {

    private final HexagonRepository hexagonRepository;
    private final HexagonAdminRepository hexagonAdminRepository;
    private final HexagonRoadRepository hexagonRoadRepository;

    public HexagonService() {
        this.hexagonRepository = new HexagonRepository();
        this.hexagonAdminRepository = new HexagonAdminRepository();
        this.hexagonRoadRepository = new HexagonRoadRepository();
    }

    public void save() {
        String path = getPath("dsmPath");
        String extension = "xyz";
        try {
            hexagonRepository.run(getFiles(path, extension));
            hexagonAdminRepository.run();
            hexagonRoadRepository.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<File> getFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        return Arrays.asList(files);
    }
}
