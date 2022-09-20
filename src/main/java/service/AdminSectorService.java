package service;

import static config.ApplicationProperties.getProperty;

import domain.Shp;
import domain.SqlReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import repository.AdminSectorRepository;

public class AdminSectorService {

    private final AdminSectorRepository adminSectorRepository;

    public AdminSectorService() {
        this.adminSectorRepository = new AdminSectorRepository();
    }

    public void storeAdminSector() {
        adminSectorRepository.saveOriginData(getSqlReader(getProperty("adminSector")), getShps());
        adminSectorRepository.procOriginData(getSqlReader(getProperty("adminSectorDivide")));
    }

    private SqlReader getSqlReader(String path) {
        File file = new File(path);
        return new SqlReader(file);
    }

    private List<Shp> getShps() {
        List<Shp> shps = new ArrayList<>();
        File[] shpFiles = findShpFiles(getProperty("adminSector.shpPath"));
        System.out.println(getProperty("adminSector.shpPath"));
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
}
