package repository;

import domain.SqlReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteQuery {

    /**
     * 테이블을 생성하는 메소드 application.properties에 명시된 경로에 있는 sql 파일을 읽어 실행한다.
     */
    public void create(Connection conn, String ddl) {
        try (Statement st = conn.createStatement()) {
            System.out.println(ddl);
            st.execute(ddl);
            conn.commit();
        } catch (SQLException e) {
            System.err.println("쿼리를 실행하는데 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public void createIndex(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.err.printf("인덱스 생성에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void alter(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.err.printf("테이블 속성을 변경하는데 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void drop(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.err.printf("테이블을 삭제하는데 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void delete(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.err.printf("tuple 삭제에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }

    public void save(Connection conn, String sql) {
        System.out.println(sql);
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
            conn.commit();
        } catch (SQLException e) {
            System.err.printf("tuple 추가에 오류가 발생했습니다.\n");
            e.printStackTrace();
        }
    }
}
