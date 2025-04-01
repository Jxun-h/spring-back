# 블로그 프로젝트 백엔드 (Spring Boot)

이 프로젝트는 블로그 서비스를 위한 백엔드 API 서버입니다. Spring Boot 프레임워크를 기반으로 구현되었으며, RESTful API를 제공합니다.

## 기술 스택

- **언어**: Java 11
- **프레임워크**: Spring Boot 2.7.x
- **데이터베이스**: MySQL 8.0
- **ORM**: Spring Data JPA + Hibernate
- **인증/인가**: Spring Security + JWT
- **빌드 도구**: Gradle

## 주요 기능

- 사용자 관리 (회원가입, 로그인, 로그아웃)
- 블로그 게시글 관리 (작성, 수정, 삭제, 조회)
- 파일 업로드
- 권한 관리

## 프로젝트 구조

```
src/main/java/its/backend/
├── global/             # 전역 설정 및 유틸리티
│   ├── config/         # 애플리케이션 설정
│   ├── response/       # 응답 모델
│   ├── security/       # 보안 설정
│   └── util/           # 유틸리티 클래스
├── web/                # 웹 관련 컴포넌트
│   ├── Controller/     # API 컨트롤러
│   ├── Service/        # 비즈니스 로직
│   ├── dto/            # 데이터 전송 객체
│   ├── entity/         # 데이터베이스 엔티티
│   └── repository/     # 데이터 액세스 계층
└── BackendApplication.java  # 애플리케이션 진입점
```

## API 엔드포인트

### 사용자 관리

- `POST /api/app/users/auth/signUp`: 회원가입
- `POST /api/app/users/auth/signIn`: 로그인
- `POST /api/app/users/auth/signOut`: 로그아웃

## 개발 환경 설정

### 로컬 개발 환경

1. JDK 11 이상 설치
2. MySQL 8.0 설치 및 데이터베이스 생성
3. 프로젝트 클론
   ```bash
   git clone [repository-url]
   cd spring-back
   ```
4. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

### 프로필 설정

- `local`: 로컬 개발 환경
- `dev`: 개발 서버 환경
- `prod`: 운영 환경

## 라이선스

이 프로젝트는 MIT 라이선스에 따라 배포됩니다.
