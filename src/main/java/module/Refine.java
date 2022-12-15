package module;

import config.ApplicationProperties;
import repository.RefineRepository;

/**
 * 모든 db 세팅 작업이 끝난 뒤 사용되지 않는 테이블 삭제
 * 삭제되는 테이블 : road_segment, admin_sector_segment
 */
public class Refine {

    private static final ApplicationProperties applicationProperties = new ApplicationProperties();

    public static void main(String[] args) {
        RefineRepository refineRepository = new RefineRepository();
        refineRepository.run();
    }
}
