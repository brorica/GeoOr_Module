package service;

import java.io.File;
import java.util.List;

/**
 * Service interface를 구현하는 개체들은 다음 2가지를 수행해야 한다.
 * 1. 지정된 경로에서 지정된 확장자를 가진 파일들을 찾는다.
 * 2. 이 파일들을 Repository에 전달해 저장한다.
 */
public interface Service {

    void save();

    List<File> getFiles(String path, String extension);
}
