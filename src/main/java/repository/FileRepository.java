package repository;

import java.io.File;
import java.sql.Connection;

/**
 * 이 interface를 구현하는 개체는 다음 2가지 역할을 해야 한다.
 * 1. 테이블 생성
 * 2. 데이터 삽입
 */
public interface FileRepository {

    void createTable(Connection conn);

    void save(Connection conn, File[] shps);

}
