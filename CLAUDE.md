# Loggit 작업 방식

이 파일은 항상 먼저 읽고 지킨다.

1. 코드를 작성하기 전에 계획(디렉터리 구조, 의존성, 파일 생성 순서)부터 제시하고 승인을 받는다.
2. 기술적 선택마다 이유, 고려한 대안, 트레이드오프를 짧게 설명한다.
3. `git init`, `git commit`, `git push`는 절대 하지 않는다. 버전 관리는 사용자가 직접 한다.
4. 코드 주석은 40자 이내, 이모지 사용 금지.
5. 명세가 모호하거나 충돌하면 추측하지 말고 질문한다. 추정으로 채운 부분은 보고 시 "추정"이라고 명시한다.
6. 라이브러리 버전은 기억에 의존하지 말고 현재 안정 버전/호환성을 확인한 뒤 계획에 적는다.
7. 이번 작업 범위를 벗어난 기능은 구현하지 않는다. 필요해 보이면 제안만 한다.
8. `.idea/` 디렉터리는 읽지도 수정하지도 않는다.

## 프로젝트 실제 상태 (2026-09 기준, 명세와 다른 부분)

- `backend/`는 IntelliJ(Spring Initializr)로 생성됨. group `com.moment`, base package `com.moment.loggit`.
- 빌드 스크립트는 **Groovy DSL**(`build.gradle`, `settings.gradle`) — Kotlin DSL로 변환하지 않기로 확정.
- Spring Boot **4.1.1** 사용 확정 (명세의 "3.x"는 낡은 가정이었음). `spring-boot-starter-webmvc`,
  `spring-boot-starter-flyway` 등 Boot 4의 모듈화된 스타터 구조를 그대로 사용.
- 설정 파일명은 `application.yml`(공통) / `application-local.yml` / `application-test.yml`로 통일.
- GitHub 레포 도메인은 엔티티 `GitRepository`, 테이블 `git_repositories`, 패키지 `gitrepo`로 명명
  (Spring Data `Repository`와 이름 혼동 방지 목적).
- 백엔드 패키지는 도메인 기준(`user`, `project`, `gitrepo`, `commit`, `task`, `note`, `summary`, `global`)으로 구성.
