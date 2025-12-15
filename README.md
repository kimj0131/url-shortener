# URL Shortener Service

Java Spring Boot 기반의 URL 단축 서비스 프로젝트입니다.


간단한 기능 구현으로 시작하여 대규모 트래픽 처리를 고려한 분산 환경 설계를 목표로 하고 있습니다.


## Tech Stack

- Java 17
- Spring Boot 4.x
- Spring Data JPA
- Database: MySQL (로컬 개발 환경에서는 H2 사용)
- Build Tool: Gradle

## Development Environment
- **IDE**: IntelliJ IDEA
- **Version Control**: Git
- **AI Assistant**: Google Gemini (Code Review & Refactoring)

## Project Roadmap

시스템을 점진적으로 개발하여 안정성과 속도를 향상시킬 계획입니다.

### 단계 1: 기본 기능 구현 (진행 중)

- [x] UUID(앞 8자리)를 활용한 URL 단축 로직 구현
- [x] 원본 URL 리다이렉트 기능
- [ ] Short URL 생성 API 및 조회 API 개발

### 단계 2: 성능 개선 및 데이터 처리 (계획)

- [ ] 데이터베이스 인덱싱을 활용한 조회 성능 개선
- [ ] Redis를 도입하여 자주 조회되는 URL 캐싱 적용
- [ ] 링크 접속 통계 기능 (User-Agent, 접속 시간 등)

### 단계 3: 안정성 및 확장성 (계획)

- [ ] 다중 인스턴스 환경에서의 동시성 이슈 제어 (Distributed Lock)
- [ ] Docker Compose를 이용한 실행 환경 구축
- [ ] CI/CD 파이프라인 구성

## Design Decisions
개발 과정에서 고민한 기술적 문제와 의사결정 과정을 기록하고 있습니다.

### URL 단축 전략 
초기 모델의 빠른 구축을 위해 **UUID의 앞 8자리를 절삭하여** 사용하는 방식을 채택했습니다.
 
* **선택 이유:** 
    이 방식은 Java 표준 라이브러리(`java.util.UUID`)를 사용하여 고유한 텍스트 문자열을 즉시 생성할 수 있기 때문입니다.

* **한계점 및 해결 방안:** 
UUID 전체가 아닌 8자리만 사용할 경우 해시 충돌이 발생할 수 있습니다. 현재는 데이터베이스의 `UNIQUE` 제약 조건을 통해 무결성을 보장하고 있고, 추후 충돌 발생 빈도가 높아질 경우 **재시도 로직**을 추가하거나 **Base62 알고리즘**으로 전환할 예정입니다.