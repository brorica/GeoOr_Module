package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 이 인터페이스를 구현한단 것은
 * 데이터베이스에 직접 삽입을 한단 클래스란 뜻이다.
 */

public interface Save <T> {

    void save(Connection conn, List<T> files) throws SQLException;

    String createQuery();
}
