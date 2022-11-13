package service;

import static config.ApplicationProperties.getProperty;

import domain.SqlReader;
import java.io.File;
import repository.frozen.FrozenRepository;

public class FrozenService {

    FrozenRepository repository = new FrozenRepository();

    public void storeFrozen() {
        File[] files = findData(getProperty("frozen"));
        repository.run(files);
    }

    private File[] findData(String path) {
        String extension = "txt";
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
