package repository.dsm;

import config.JdbcTemplate;
import geoUtil.UberH3;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;
import repository.FileRepository;

public class TestDsmRepository implements FileRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private final String tableName;
    private final UberH3 h3;

    public TestDsmRepository(String tableName){
        this.tableName = tableName;
        this.h3 = new UberH3();
    }

    public void run(List<File> dsms) throws IOException {
        makeHexagonMap(dsms);
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveH3(conn);
            createIndex(conn);
            createClusterIndex(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeHexagonMap(List<File> dsms) throws IOException {
        for (File dsm : dsms) {
            System.out.print(dsm.getName() + " save... ");
            readDsm(dsm);
            test();
        }
    }

    private void readDsm(File dsm) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(dsm));
        String line;
        double lat, lon;
        int height;
        while ((line = reader.readLine()) != null) {
            String[] s = line.split(" ");
            lat = Double.parseDouble(s[0]);
            lon = Double.parseDouble(s[1]);
            height = Integer.parseInt(s[2]);
            h3.addH3Address(lat, lon, height);
        }
        reader.close();
    }

    private void test() {
        System.out.println(h3.getH3Map().size());
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "   the_geom geometry(Polygon, 4326),\n"
            + "   address bigint,\n"
            + "   height integer,\n"
            + "   sig_cd integer)";
        executeQuery.create(conn, ddl);
    }

    private void saveH3(Connection conn) {
        long startTime, endTime;
        startTime = System.currentTimeMillis();
        TestSaveDsm saveDsm = new TestSaveDsm(tableName, h3);
        saveDsm.save(conn);
        endTime = System.currentTimeMillis();
        System.out.println(" cost : " + (endTime - startTime) / 1000 + "s");
    }

    private void createIndex(Connection conn) {
        String indexName = "testdsm_sig_cd_index";
        String sql = "CREATE INDEX " + indexName + " ON testdsm USING btree(sig_cd)";
        executeQuery.createIndex(conn, sql);
    }

    private void createClusterIndex(Connection conn) {
        String indexName = "testdsm_sig_cd_index";
        String sql = "CLUSTER " + tableName + " USING "+ indexName;
        executeQuery.createIndex(conn, sql);
    }
}
