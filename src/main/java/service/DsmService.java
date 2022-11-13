package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import repository.dsm.DsmRepository;

public class DsmService implements Service {

    private final DsmRepository origin = new DsmRepository();

    @Override
    public void save() {
        String path = getProperty("dsm");
        String extension = "xyz";
        origin.run(getFiles(path, extension));
    }

    @Override
    public File[] getFiles(String path, String extension) {
        return new File[0];
    }

}
