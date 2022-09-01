package repository.dsm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
