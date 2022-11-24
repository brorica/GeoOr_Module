package repository;

/**
 * 이 클래스를 상속받는 클래스들은 행정 구역 테이블과 관련된 클래스다.
 * 주의할 점은 원본 행정 구역 테이블이 아닌
 * 분할된 형태의 행정 구역 테이블이기 때문에
 * 이 테이블과 공간 쿼리 등을 수행하기 전,
 * 사전에 명시된 테이블이 있는지 확인해야 한다.
 */
public class RelateAdminSector {

    private final String adminSectorSegmentTableName = "admin_sector_segment ";

    protected String getAdminSectorSegmentTableName() {
        return adminSectorSegmentTableName;
    }
}
