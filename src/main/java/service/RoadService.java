package service;

import static config.ApplicationProperties.getProperty;

import domain.SqlReader;
import domain.HillShadeGrid;
import domain.Road;
import domain.Shp;
import java.io.File;
import repository.RoadRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoadService {

    private final RoadRepository roadRepository;
    private List<Road> roads = new ArrayList<>();;

    public RoadService() {
        roadRepository = new RoadRepository();
    }

    public void storeRoad() {
        roadRepository.createTable(getSqlReader());
        roadRepository.save(getShps());
    }

    private SqlReader getSqlReader() {
        File file = new File(getProperty("road.createFilePath"));
        return new SqlReader(file);
    }

    private List<Shp> getShps() {
        List<Shp> shps = new ArrayList<>();
        File[] shpFiles = findShpFiles(getProperty("road.shpPath"));
        for (File file : shpFiles) {
            try {
                shps.add(new Shp(file));
            } catch (IOException e) {
                System.err.printf("%s 식별 과정에 오류가 발생했습니다.\n", file.getName());
                e.printStackTrace();
            }
        }
        return shps;
    }

    private File[] findShpFiles(String shpPath) {
        File directory = new File(shpPath);
        String extension = "shp";
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

    /**
     * 도로의 hillshade 계산이 올바르게 수행되는지 테스트하는 메소드
     * 매개변수로는 시도코드 앞 2자리를 받는다.
     * ex) 세종시가 36110이니, 인자로 36을 준다.
     */
    public void calcHillShade(String sigCode) {
        int baseIndex = roadRepository.findBySigCode(sigCode, roads);
        roadRepository.findByOverlapGrid(sigCode,baseIndex, roads, createTestHillShadeGrids());
        roadRepository.updateHillShade(roads);
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
