package repository.dsm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 영역 밖의 dsm 좌표 모두 삭제
 */
public class DeleteUnUseDsm {

    public void delete(Connection conn) {
        String sql = "DELETE FROM dsm_temp WHERE sig_cd is NULL";
        try (Statement st = conn.createStatement()) {
            System.out.println("delete dsm_temp table sig_cd null data..");
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void drop(Connection conn) {
        String sql = "DROP table dsm_temp";
        try (Statement st = conn.createStatement()) {
            System.out.println("Drop dsm_temp table...");
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
