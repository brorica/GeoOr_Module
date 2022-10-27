package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteQuery {

    public void createIndex(Connection conn, String sql)  {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.err.printf("인덱스 생성에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }
}
