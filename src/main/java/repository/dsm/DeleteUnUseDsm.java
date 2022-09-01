package repository.dsm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 영역 밖의 dsm 좌표 모두 삭제
 */
public class DeleteUnUseDsm {

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM dsm_temp WHERE sig_cd is NULL";
        try (Statement st = conn.createStatement()) {
            System.out.println(sql);
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    public void drop(Connection conn) {
        String sql = "DROP table dsm_temp";
        try (Statement st = conn.createStatement()) {
            System.out.println(sql);
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
