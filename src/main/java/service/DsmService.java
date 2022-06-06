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
        dsmRepository.createTable(getSqlReader());
        dsmRepository.save(findDsms());
        // 아래 메소드는 saveDsmOverlaps 작업이 끝난 후, PgAdmin에서 행정구역이 제대로 들어간게 확인되면 주석 해제
        //dsmRepository.delete();
    }

    private SqlReader getSqlReader() {
        File file = new File(getProperty("dsm.createFilePath"));
        return new SqlReader(file);
    }

    private File[] findDsms() {
        String path = getProperty("dsm.path");
        String extension = "xyz";
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
