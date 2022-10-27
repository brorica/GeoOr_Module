package repository.dsm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 행정 구역 외의 dsm 격자들을 모두 제거한 후
 * 원활한 인덱싱을 위해 primary key 부여
 */
public class CreatePrimaryKey {

    public void create(Connection conn) {
        String sql = "ALTER TABLE dsm ADD COLUMN id SERIAL PRIMARY KEY;";
        try (Statement st = conn.createStatement()) {
            System.out.println(sql);
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
