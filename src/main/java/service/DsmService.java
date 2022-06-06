package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import java.util.ArrayList;
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

    public void setDsmSigCode() {
        HillShadeService hillShadeService = new HillShadeService();
        ArrayList<Double> coordinates = hillShadeService.getCoordinates(354211, 574118);
        dsmRepository.updateHillShade(coordinates);
    }

}
