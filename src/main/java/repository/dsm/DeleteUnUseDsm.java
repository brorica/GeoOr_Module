package repository.dsm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 영역 밖의 dsm 좌표 모두 삭제
 */
public class DeleteUnUseDsm {

    public void delete(Connection conn) {
        String sql = "DELETE FROM dsm WHERE sig_cd is NULL";
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
