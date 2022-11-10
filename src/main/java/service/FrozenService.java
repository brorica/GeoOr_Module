package service;

import static config.ApplicationProperties.getProperty;

import domain.SqlReader;
import java.io.File;
import repository.frozen.FrozenRepository;

public class FrozenService {

    FrozenRepository repository = new FrozenRepository();

    public void storeFrozen() {
        SqlReader createSql = getSqlReader(getProperty("frozen"));
        File[] files = findData(getProperty("frozen.dataPath"));
        repository.run(createSql, files);
    }

    private SqlReader getSqlReader(String path) {
        File file = new File(path);
        return new SqlReader(file);
    }

    private File[] findData(String path) {
        String extension = "txt";
        File directory = new File(path);
        return directory.listFiles((dir, name) -> name.endsWith(extension));
    }

}
