package repository;

import config.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main.class 실행 기준
 * 이 클래스는 맨 마지막에 실행돼야 한다.
 */
public class RefineRepository {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private ExecuteQuery executeQuery = new ExecuteQuery();

    public void run() {
        try (Connection conn = jdbcTemplate.getConnection()) {
            dropRoadSegment(conn);
            dropAdminSectorSegment(conn);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropRoadSegment(Connection conn) {
        executeQuery.drop(conn, "road_segment");
    }

    private void dropAdminSectorSegment(Connection conn) {
        executeQuery.drop(conn, "admin_sector_segment");
    }
}
