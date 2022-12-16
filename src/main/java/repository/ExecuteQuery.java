package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteQuery {

    public void create(Connection conn, String ddl) {
        try (Statement st = conn.createStatement()) {
            System.out.println(ddl);
            st.execute(ddl);
        } catch (SQLException e) {
            System.err.println("쿼리를 실행하는데 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public void createIndex(Connection conn, String index, String table, String indexType, String column) {
        String sql = "CREATE INDEX " + index + " ON " + table + " USING " + indexType +"(" + column + ")";
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.printf("인덱스 생성에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    /**
     * 클러스터 인덱스 생성
     * 물리적으로 재정렬 하기 때문에 공간이 충분해야 한다.
     */
    public void createIndex(Connection conn, String table, String index) {
        String sql = "CLUSTER " + table + " USING " + index;
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.printf("인덱스 생성에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void alter(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.printf("테이블 속성을 변경하는데 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void drop(Connection conn, String table) {
        String sql = "DROP TABLE " + table;
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.printf("테이블을 삭제하는데 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void delete(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.printf("tuple 삭제에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void save(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.printf("tuple 추가에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }
}
