package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import domain.SqlReader;
import repository.dsm.DivideDsmRepository;
import repository.dsm.OriginDsmRepository;

public class DsmService {

    private final OriginDsmRepository origin = new OriginDsmRepository();
    private final DivideDsmRepository divide = new DivideDsmRepository();

    public void storeDsm() {
        origin.run(getSqlReader(getProperty("dsmTemp")), findDsms(getProperty("dsm.path")));
        divide.run(getSqlReader(getProperty("dsm")));

    }

    private SqlReader getSqlReader(String path) {
        File file = new File(path);
        return new SqlReader(file);
    }

    private File[] findDsms(String path) {
        String extension = "xyz";
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
