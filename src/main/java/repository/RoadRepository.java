package repository;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import domain.Shp;
import domain.SqlReader;
import repository.dsm.DeleteUnUseDsm;
import repository.road.DropRoadDump;
import repository.road.SaveRoad;

public class RoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void runSQL(SqlReader sqlReader) {
        TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveRoad(List<Shp> shps) {
        SaveRoad saveRoad = new SaveRoad();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveRoad.save(conn, shps);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createIndex(SqlReader sqlReader) {
        CreateIndex createIndex = new CreateIndex();
        try (Connection conn = jdbcTemplate.getConnection()) {
            createIndex.create(conn, sqlReader);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropDumpTable() {
        DropRoadDump dropRoadDump = new DropRoadDump();
        try (Connection conn = jdbcTemplate.getConnection()) {
            dropRoadDump.drop(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
