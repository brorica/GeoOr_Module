package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import java.util.Arrays;
import java.util.List;
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
    public List<File> getFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        return Arrays.asList(files);
    }

}
