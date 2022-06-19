package repository.dsm;

import domain.SqlReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SaveSortedDsm {

    public void save(Connection conn, SqlReader sqlReader) {
        try (Statement st = conn.createStatement()) {
            String query = sqlReader.getContents();
            System.out.println(query);
            st.execute(query);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("dsm_temp table을 정렬한 결과를 복제하는 과정에서 오류가 발생했습니다.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("dsm_temp table을 정렬하는 쿼리를 읽는데 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

}
