package repository.road;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DropRoadDump {

    public void drop(Connection conn) {
        String sql = "DROP table road_dump";
        try (Statement st = conn.createStatement()) {
            System.out.println("Drop road_dump table...");
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
