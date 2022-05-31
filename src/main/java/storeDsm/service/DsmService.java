package storeDsm.service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import storeDsm.repository.DsmRepository;
import util.CreateSqlReader;

public class DsmService {

    private final DsmRepository dsmRepository;

    public DsmService() {
        dsmRepository = new DsmRepository();
    }

    public void run() {
        dsmRepository.createTable(getSqlReader());
        dsmRepository.save(findDsms());
    }

    private CreateSqlReader getSqlReader() {
        File file = new File(getProperty("dsm.createFilePath"));
        return new CreateSqlReader(file);
    }

    /**
     * 지정한 경로 내의 모든 DSM 파일 가져옴
     */
    private File[] findDsms() {
        String path = getProperty("dsm.path");
        String extension = "xyz";
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }
}
