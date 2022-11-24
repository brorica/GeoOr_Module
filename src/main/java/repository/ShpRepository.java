package repository;

import domain.Shp;
import java.util.List;

/**
 * 이 인터페이스를 구현하는 것은
 * 사용되는 데이터가 shp 파일이란 것이다.
 */
public interface ShpRepository {

    void run(List<Shp> shps);
}
