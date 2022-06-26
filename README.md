# GeoOr_Module
hillshade 알고리즘을 실행하기 전 필요한 작업 모듈들

처음 프로그램을 받아서 실행한다면 postgresql jar파일을 프로젝트에 포함시켜 주세요.
File ->Project Structure -> Libraries -> Add -> ./lib -> postgresql-42.3.1.jar

application.properties에서 데이터베이스 접근에 필요한 환경변수를 확인해주세요.

도로 SHP 출처 : http://data.nsdi.go.kr/dataset/20180918ds00072 (22.06.13 기준 ESPG:5181)

행정구역 SHP 출처 : http://data.nsdi.go.kr/dataset/15144 (22.05월 기준 EPSG:5179)

도로 좌표계는 계속 바뀌니 prj를 확인해주세요