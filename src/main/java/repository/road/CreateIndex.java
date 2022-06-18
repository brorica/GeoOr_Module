package repository.road;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateIndex {

    public void create(Connection conn)  {
        try (Statement st = conn.createStatement()) {
            String query = CreateQuery();
            System.out.println(query);
            st.execute(query);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("sql 파일을 읽는데 오류가 발생했습니다. 파일을 확인해 주세요.");
            e.printStackTrace();
        }
    }

    private String CreateQuery() {
        return "CREATE INDEX road_centroid_index ON road_centroid USING gist(centroid);";
    }

}
