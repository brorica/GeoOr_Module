package road.storeRoad.repository;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import road.storeRoad.domain.Shp;
import storeDsm.repository.TableCreator;
import util.CreateSqlReader;

public class StoreRoadRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void createTable(CreateSqlReader sqlReader) {
        storeDsm.repository.TableCreator tableCreator = new TableCreator();
        try (Connection conn = jdbcTemplate.getConnection()) {
            tableCreator.create(conn, sqlReader);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(List<Shp> shps) {
        SaveRoad saveRoad = new SaveRoad();
        try (Connection conn = jdbcTemplate.getConnection()) {
            saveRoad.save(conn, shps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
