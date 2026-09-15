# Loggit

GitHub 레포지토리의 커밋을 수집해 "오늘/이번 주에 무슨 작업을 했는지" 기록하고
확인하는 다중 사용자 웹 서비스.

## 스택

- Backend: Java 21, Spring Boot 4.1.1 (Gradle, Groovy DSL)
- Persistence: Spring Data JPA, PostgreSQL, Flyway
- Frontend: React + TypeScript + Vite
- 로컬 인프라: Docker Compose (PostgreSQL)

## 로컬 실행

### 1. 환경 변수 준비

```
cp .env.example .env
```

`.env`에 `DB_PASSWORD` 등 값을 채운다.

### 2. PostgreSQL 기동

```
docker compose up -d
docker compose ps   # postgres가 healthy인지 확인
```

### 3. 백엔드 실행

Spring Boot는 `.env` 파일을 자동으로 읽지 않으므로, 로컬 실행 전 셸에 값을 로드한다.

```
cd backend
set -a; source ../.env; set +a
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

IntelliJ에서 실행할 경우 EnvFile 플러그인 등으로 `.env`를 로드하고
Active profile을 `local`로 설정한다 (실행 설정은 직접 구성).

기동 시 Flyway가 `V1__init_schema.sql`을 적용하고,
`spring.jpa.hibernate.ddl-auto=validate`로 엔티티-스키마 일치를 검증한다.

### 4. 프론트엔드 실행

```
cd frontend
npm install
npm run dev
```

`/api`로 시작하는 요청은 Vite 개발 서버 프록시를 통해
`http://localhost:8080`(백엔드, context-path `/api`)로 전달된다.
CORS 설정은 필요하지 않다.

## IntelliJ에서 열기

1. IntelliJ에서 `Open`으로 저장소 루트(`Loggit/`)를 연다.
2. `backend/build.gradle`을 우클릭 → `Link Gradle Project` (또는 팝업으로 뜨는
   "Gradle 프로젝트를 가져올까요?" 알림에서 `Import Gradle Project` 선택)로
   백엔드를 Gradle 프로젝트로 연결한다.
3. 프론트엔드는 별도의 Node.js 실행 설정으로 `frontend/` 안에서 `npm run dev`를
   실행하거나, 터미널에서 직접 실행한다.

## 프로젝트 구조

```
Loggit/
├─ backend/    Spring Boot API (context-path: /api)
├─ frontend/   React + Vite
├─ docs/
├─ docker-compose.yml
├─ .env.example
└─ CLAUDE.md   작업 방식 규칙
```
