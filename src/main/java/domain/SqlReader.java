package domain;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SqlReader implements Closeable {

    private final BufferedReader reader;

    public SqlReader(File file) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(fileReader);
    }

    public String getContents() throws IOException {
        StringBuilder query = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            query.append(line);
        }
        return query.toString();
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedReader getReader() {
        return reader;
    }
}
