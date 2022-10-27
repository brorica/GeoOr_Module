package repository.adminSector;

import config.JdbcTemplate;
import domain.Shp;
import domain.SqlReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import repository.ExecuteQuery;
import repository.adminSector.SaveAdminSector;

/**
 * 원본 행정 구역 shp 저장
 * DivideAdminSectorRepository 를 통해 R-Tree 인덱싱에 적합하게 분할한다.
 */
public class OriginAdminSectorRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run(SqlReader createSql, List<Shp> shps) {
        try (Connection conn = jdbcTemplate.getConnection()) {
            createTable(conn, createSql);
            saveAdminSector(conn, shps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable(Connection conn, SqlReader sqlReader) {
        executeQuery.create(conn, sqlReader);
    }

    private void saveAdminSector(Connection conn, List<Shp> shps) {
        SaveAdminSector saveAdminSector = new SaveAdminSector();
        saveAdminSector.save(conn, shps);
    }

}
