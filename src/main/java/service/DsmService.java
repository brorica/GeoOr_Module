package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import repository.dsm.DsmRepository;

public class DsmService implements Service {

    private final DsmRepository origin;
    private final String originTableName = "dsm";

    public DsmService() {
        this.origin = new DsmRepository(originTableName);
    }

    @Override
    public void save() {
        String path = getProperty("dsm");
        String extension = "xyz";
        origin.run(getFiles(path, extension));
    }

    @Override
    public File[] getFiles(String path, String extension) {
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
