package repository.adminSector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DividePolygon {

    public void divide(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            String sql = getSql();
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    private String getSql() {
        String sql = "insert into admin_sector_divide(the_geom, adm_sect_cd) "
            + "select ST_Subdivide(ST_CollectionExtract(ST_MakeValid(the_geom), 3)), adm_sect_cd from admin_sector";
        System.out.println(sql);
        return sql;
    }
}
