package service;

import static config.ApplicationProperties.getProperty;

import domain.SqlReader;
import java.io.File;
import repository.dsm.DsmRepository;

public class DsmService {

    private final DsmRepository origin = new DsmRepository();

    public void storeDsm() {
        origin.run(getSqlReader(getProperty("dsm")), findDsm(getProperty("dsm.path")));
    }

    private SqlReader getSqlReader(String path) {
        File file = new File(path);
        return new SqlReader(file);
    }

    private File[] findDsm(String path) {
        String extension = "xyz";
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
