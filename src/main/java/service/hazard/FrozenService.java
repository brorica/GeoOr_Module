package service.hazard;

import static config.ApplicationProperties.getPath;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import repository.hazard.frozen.FrozenRepository;

public class FrozenService {

    private final FrozenRepository repository = new FrozenRepository();

    public void save() {
        String path = getPath("frozenPath");
        String extension = "txt";
        repository.run(getFiles(path, extension));
    }

    public List<File> getFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        return Arrays.asList(files);
    }
}
