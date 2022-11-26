package service;

import static config.ApplicationProperties.getProperty;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import repository.dsm.DsmRepository;
import repository.dsm.TestDsmRepository;

public class DsmService implements Service {

    private final DsmRepository origin;
    private final TestDsmRepository test;
    private final String originTableName = "dsm";

    public DsmService() {
        this.origin = new DsmRepository(originTableName);
        this.test = new TestDsmRepository("testDsm");
    }

    @Override
    public void save() {
        String path = getProperty("dsm");
        String extension = "xyz";
        //origin.run(getFiles(path, extension));
        try {
            test.run(getFiles(path, extension));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<File> getFiles(String path, String extension) {
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(extension));
        return Arrays.asList(files);
    }
}
