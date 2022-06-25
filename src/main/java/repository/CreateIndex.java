package repository;

import domain.SqlReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateIndex {

    public void create(Connection conn, SqlReader sqlReader)  {
        try (Statement st = conn.createStatement()) {
            String query = sqlReader.getContents();
            System.out.println(query);
            st.execute(query);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("인덱스 쿼리를 실행하는데 오류가 발생했습니다.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("인덱스 쿼리 파일을 읽는데 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

}
