package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import repository.DsmRepository;
import domain.SqlReader;

public class DsmService {

    private final DsmRepository dsmRepository;

    public DsmService() {
        dsmRepository = new DsmRepository();
    }

    public void storeDsm() {
        dsmRepository.createTable(getSqlReader(getProperty("dsmTemp")));
        dsmRepository.saveDsmTemp(findDsms(getProperty("dsm.path")));
        dsmRepository.deleteNullSigCd();
        dsmRepository.saveSortedDsm(getSqlReader(getProperty("dsm")));
        dsmRepository.createIndex(getSqlReader(getProperty("dsmSigCdIndex")));
        dsmRepository.dropDsmTempTable();
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
