# 1. 베이스 이미지 (자바 17이 설치된 리눅스 환경)
FROM amazoncorretto:17-alpine-jdk

# 2. 작업 디렉토리 설정 (컨테이너 내부의 폴더)
WORKDIR /app

# 3. 빌드된 JAR 파일을 컨테이너로 복사
# 나중에 ./gradlew build를 하면 bulid/lib에 jar 생성
COPY build/libs/*-SNAPSHOT.jar app.jar

# 4. 실행 명령어 (컨테이너가 켜지면 해당 명령어를 실행)
ENTRYPOINT ["java", "-jar", "app.jar"]