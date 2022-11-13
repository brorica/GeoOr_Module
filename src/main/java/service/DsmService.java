package service;

import static config.ApplicationProperties.getProperty;

import domain.SqlReader;
import java.io.File;
import repository.dsm.DsmRepository;

public class DsmService {

    private final DsmRepository origin = new DsmRepository();

    public void storeDsm() {
        origin.run(findDsm(getProperty("dsm")));
    }

    private File[] findDsm(String path) {
        String extension = "xyz";
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
