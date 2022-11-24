package repository;

import java.io.File;

/**
 * 이 인터페이스를 구현하는 것은
 * 사용되는 데이터가 shp가 아니라는 것이다.
 */
public interface FileRepository {

    void run(File[] files);
}
