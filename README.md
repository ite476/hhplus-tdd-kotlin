# HHPlus TDD Assignment

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── kotlin/io/hhplus/tdd/
│   │   ├── TddApplication.kt          # Spring Boot 메인 애플리케이션
│   │   ├── ApiControllerAdvice.kt     # 전역 예외 처리
│   │   ├── point/                     # 포인트 도메인
│   │   │   ├── PointController.kt     # 포인트 API 컨트롤러
│   │   │   ├── UserPoint.kt          # 사용자 포인트 데이터 클래스
│   │   │   └── PointHistory.kt       # 포인트 내역 데이터 클래스
│   │   └── database/                  # 데이터 접근 계층
│   │       ├── UserPointTable.kt      # 사용자 포인트 테이블
│   │       └── PointHistoryTable.kt   # 포인트 내역 테이블
│   └── resources/
│       └── application.yml            # 애플리케이션 설정
└── test/
    └── kotlin/io/hhplus/tdd/
        └── point/                     # 포인트 도메인 테스트
            └── PointControllerTests.kt # 포인트 컨트롤러 테스트
```

## 🎯 구현 기능

### 포인트 CRUD 기능
- **문서**: [docs/issue/#1/README.md](docs/issue/#1/README.md)
- **브랜치**: `feature/1.0.0/#1-point-crud`

## 🚀 실행 방법

```bash
# 애플리케이션 실행
./gradlew bootRun

# 테스트 실행
./gradlew test

# 빌드
./gradlew build
```

## 🧪 테스트 결과 리포트 확인

테스트 실행 후, 아래 명령어로 테스트/커버리지 리포트(HTML)를 바로 열어볼 수 있습니다.

### 테스트 결과 리포트 (Test Report)
- 위치: `build/reports/tests/test/index.html`

#### Mac/Linux
```bash
open build/reports/tests/test/index.html
```
#### Windows (PowerShell)
```powershell
start build/reports/tests/test/index.html
```

### 코드 커버리지 리포트 (JaCoCo Coverage)
- 위치: `build/reports/jacoco/test/html/index.html`

#### Mac/Linux
```bash
open build/reports/jacoco/test/html/index.html
```
#### Windows (PowerShell)
```powershell
start build/reports/jacoco/test/html/index.html
```

## 📝 개발 가이드

### TDD 진행 원칙
1. **Red**: 실패하는 테스트 작성
2. **Green**: 최소한의 코드로 테스트 통과
3. **Refactor**: 코드 개선

### 커밋 컨벤션
- `test: {기능명} 테스트 추가`
- `feat: {기능명} 구현`
- `refactor: {기능명} 개선`
- `docs: {문서명} 업데이트`

### 브랜치 전략
- `feature/{버전}/#{이슈번호}-{기능명}` 형식 사용
- 예: `feature/1.0.0/#1-point-crud` 