package service;

import static config.ApplicationProperties.getProperty;
import java.io.File;
import repository.frozen.FrozenRepository;

public class FrozenService implements Service {

    FrozenRepository repository = new FrozenRepository();

    public void save() {
        String path = getProperty("frozen");
        String extension = "txt";
        repository.run(getFiles(path, extension));
    }

    public File[] getFiles(String path, String extension) {
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
