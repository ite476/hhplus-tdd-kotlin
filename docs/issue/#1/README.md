# Issue #1: 포인트 CRUD 기능 구현

## 📋 요구사항

### 1. 포인트 조회 API
- **엔드포인트**: `GET /point/{id}`
- **기능**: 특정 유저의 포인트 조회
- **응답**: `UserPoint(id, point, updateMillis)`

### 2. 포인트 충전 API
- **엔드포인트**: `PATCH /point/{id}/charge`
- **기능**: 특정 유저의 포인트 충전
- **요청**: 충전할 금액 (Long)
- **응답**: `UserPoint(id, point, updateMillis)`

### 3. 포인트 사용 API
- **엔드포인트**: `PATCH /point/{id}/use`
- **기능**: 특정 유저의 포인트 사용
- **요청**: 사용할 금액 (Long)
- **응답**: `UserPoint(id, point, updateMillis)`

### 4. 포인트 내역 조회 API
- **엔드포인트**: `GET /point/{id}/histories`
- **기능**: 특정 유저의 포인트 충전/사용 내역 조회
- **응답**: `List<PointHistory>`

## 🧪 TDD 진행 계획

### **Red-Green-Refactor 사이클**

각 기능별로 다음 사이클을 반복합니다:

1. **Red**: 실패하는 테스트 작성
2. **Green**: 최소한의 코드로 테스트 통과
3. **Refactor**: 코드 개선

### **기능별 진행 순서**

#### **1단계: 포인트 조회 기능**
- [ ] Red: 포인트 조회 테스트 작성
- [ ] Green: 포인트 조회 기능 구현
- [ ] Refactor: Service 계층 분리

#### **2단계: 포인트 충전 기능**
- [ ] Red: 포인트 충전 테스트 작성
- [ ] Green: 포인트 충전 기능 구현
- [ ] Refactor: Repository 계층 분리

#### **3단계: 포인트 사용 기능**
- [ ] Red: 포인트 사용 테스트 작성
- [ ] Green: 포인트 사용 기능 구현
- [ ] Refactor: 비즈니스 로직 개선

#### **4단계: 포인트 내역 조회 기능**
- [ ] Red: 포인트 내역 조회 테스트 작성
- [ ] Green: 포인트 내역 조회 기능 구현
- [ ] Refactor: 최적화

## 📝 커밋 전략

각 기능별로 3개의 커밋을 생성합니다:

1. `test: {기능명} 테스트 추가`
2. `feat: {기능명} 구현`
3. `refactor: {기능명} 개선`

예시:
- `test: 포인트 조회 기능 테스트 추가`
- `feat: 포인트 조회 기능 구현`
- `refactor: 포인트 조회 기능 Service 계층 분리`

## 🔧 기술적 고려사항

### 테스트 전략
- **Spring Boot Test Framework** 활용
  - `@WebMvcTest` + `@MockBean` 조합
  - `MockMvc`를 통한 HTTP 요청 테스트
  - JSON 응답 검증

### 테스트 케이스
#### **포인트 조회 테스트**
- ✅ 정상 조회 (포인트가 있는 경우)
- ❌ 존재하지 않는 유저 조회
- ❌ 잘못된 ID 형식

#### **포인트 충전 테스트**
- ✅ 정상 충전
- ✅ 충전 후 포인트 증가 확인
- ❌ 음수 금액 충전
- ❌ 존재하지 않는 유저 충전
- ❌ 잘못된 요청 형식

#### **포인트 사용 테스트**
- ✅ 정상 사용 (충분한 포인트)
- ✅ 사용 후 포인트 감소 확인
- ❌ 부족한 포인트 사용
- ❌ 음수 금액 사용
- ❌ 존재하지 않는 유저 사용

#### **포인트 내역 조회 테스트**
- ✅ 정상 내역 조회
- ✅ 빈 내역 조회
- ❌ 존재하지 않는 유저 내역 조회

## 🔗 관련 링크

- **메인 README**: [../../README.md](../../README.md)
- **브랜치**: `feature/1.0.0/#1-point-crud`
- **관련 파일**:
  - `src/main/kotlin/io/hhplus/tdd/point/PointController.kt`
  - `src/test/kotlin/io/hhplus/tdd/point/PointControllerTests.kt` 