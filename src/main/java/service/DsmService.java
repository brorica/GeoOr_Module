package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import repository.dsm.DsmRepository;
import domain.SqlReader;

public class DsmService {

    private final DsmRepository dsmRepository;

    public DsmService() {
        dsmRepository = new DsmRepository();
    }

    public void storeDsm() {
        dsmRepository.saveOriginData(getSqlReader(getProperty("dsmTemp")), findDsms(getProperty("dsm.path")));
        dsmRepository.procOriginData(getSqlReader(getProperty("dsm")));

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
