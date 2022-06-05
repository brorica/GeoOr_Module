package service;

import static config.ApplicationProperties.getProperty;

import domain.HillShadeGrid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import repository.DsmRepository;
import domain.SqlReader;

public class DsmService {

    private final DsmRepository dsmRepository;
    private final String[] sigCodes  = {
        "11", "26", "27", "28",
        "29", "30", "31", "36", "41",
        "42", "43", "44", "45", "46",
        "47", "48", "50"
    };

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

    /**
     * 이 작업은 HillShadeGrid 리스트가 전달됐다는 가정 하에 작동하는 코드다.
     * dsm.testpath 환경변수에 지정된 파일을 읽는다.
     */
    public void setDsmSigCode() {
        dsmRepository.updateHillShade(createTestHillShadeGrids());
    }

    private List<HillShadeGrid> createTestHillShadeGrids() {
        List<HillShadeGrid> hillShadeGrids = new ArrayList<>();
        String path = getProperty("dsm.testPath");
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] points = line.split(" ");
                hillShadeGrids.add(new HillShadeGrid(
                    Double.parseDouble(points[0]),
                    Double.parseDouble(points[1]),
                    Double.parseDouble(points[2]),
                    Double.parseDouble(points[3]),
                    Double.parseDouble(points[4]),
                    Double.parseDouble(points[5])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hillShadeGrids;
    }
}
