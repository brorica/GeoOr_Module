package service.hazard;

import static config.ApplicationProperties.getProperty;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import repository.hazard.frozen.FrozenRepository;
import service.Service;

public class FrozenService implements Service {

    private final FrozenRepository repository;

    private final String originTableName = "frozen";

    public FrozenService() {
        this.repository = new FrozenRepository(originTableName);
    }

    @Override
    public void save() {
        String path = getProperty("frozen");
        String extension = "txt";
        repository.run(getFiles(path, extension));
    }

    @Override
    public List<File> getFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        return Arrays.asList(files);
    }
}
