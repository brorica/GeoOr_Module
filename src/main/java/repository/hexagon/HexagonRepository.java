package repository.hexagon;

import config.JdbcTemplate;
import domain.HexagonMap;
import geoUtil.UberH3;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;

public class HexagonRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    private final String indexName = "hexagon_id_index";

    private final UberH3 h3 = new UberH3();
    private final HexagonMap hexagonMap = new HexagonMap();

    private final String tableName;

    public HexagonRepository(String tableName){
        this.tableName = tableName;
    }

    public void run(List<File> dsms) throws IOException {
        makeHexagonMap(dsms);
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn);
            saveHexagon(conn);
            createIndex(conn);
            createClusterIndex(conn);
            conn.commit();
            hexagonMap.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeHexagonMap(List<File> dsms) throws IOException {
        for (File dsm : dsms) {
            System.out.print(dsm.getName() + " read... ");
            readDsm(dsm);
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
            hexagonMap.addCellIndex(h3.getLatLngToCell(lat, lon), height);
        }
        reader.close();
    }

    private void createTable(Connection conn) {
        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
            + "   the_geom geometry(Polygon, 4326) NOT NULL,\n"
            + "   id bigint PRIMARY KEY,\n"
            + "   height INTEGER NOT NULL)";
        executeQuery.create(conn, ddl);
    }

    private void saveHexagon(Connection conn) throws SQLException {
        long startTime, endTime;
        startTime = System.currentTimeMillis();
        SaveHexagon saveDsm = new SaveHexagon(tableName);
        saveDsm.save(conn, hexagonMap);
        endTime = System.currentTimeMillis();
        System.out.println(" cost : " + (endTime - startTime) / 1000 + "s");
    }

    private void createIndex (Connection conn) {
        executeQuery.createIndex(conn, indexName, tableName, "btree", "id");
    }

    private void createClusterIndex (Connection conn) {
        executeQuery.createIndex(conn, tableName, indexName);
    }
}
