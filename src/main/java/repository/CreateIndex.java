package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateIndex {

    public void create(Connection conn, String sql)  {
        try (Statement st = conn.createStatement()) {
            System.out.println(sql);
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("인덱스 쿼리를 실행하는데 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

}
