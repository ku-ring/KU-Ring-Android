<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 도메인 레이어: AI 에이전트 안내

도메인 레이어는 비즈니스 로직, 유즈케이스, 리포지토리 인터페이스를 정의합니다. 각 모듈은 Android 의존성을 최소화하고, 순수 Kotlin 로직과 데이터 계약에 집중합니다.

## 모듈 개요

| 모듈 | 목적 | 주요 클래스 |
|--------|---------|------------|
| `academicevent/` | 학사 일정 유즈케이스 및 리포지토리 인터페이스 | `AcademicEventRepository` (interface), `GetAcademicEventsUseCase` |
| `club/` | 동아리/단체 도메인 모델 및 리포지토리 | `ClubRepository` (interface), club use cases |
| `user/` | 사용자 프로필 유즈케이스 및 리포지토리 인터페이스 | `UserRepository` (interface), `RegisterUserUseCase` |
| `noticecomment/` | 공지 댓글 유즈케이스 및 리포지토리 인터페이스 | `NoticeCommentRepository` (interface), comment use cases |
| `notification/` | 푸시 알림 리포지토리 인터페이스 | `NotificationRepository` (interface), notification preferences |
| `report/` | 콘텐츠 신고/피드백 도메인 | Report entities, `ReportRepository` (interface) |
| `place/` | 캠퍼스 장소 리포지토리 인터페이스 | `PlaceRepository` (interface), place data models |
| `navigation/` | 피처 간 공유 네비게이션 라우트 정의 | Route state management, navigator interface |

## 모듈 구조

### 표준 도메인 모듈 패턴

각 도메인 모듈(`navigation` 제외)은 아래 구조를 따릅니다:

```text
domain/{module}/
├── build.gradle.kts           # Convention: kuring("kotlin")
├── src/main/java/com/ku_stacks/ku_ring/domain/{module}/
│   ├── repository/
│   │   └── {Name}Repository.kt    # 인터페이스만 존재 (구현체 없음)
│   └── usecase/
│       └── {Name}UseCase.kt       # 주입된 리포지토리를 사용하는 비즈니스 로직
└── src/test/java/...              # 단위 테스트
```

### 리포지토리 인터페이스

리포지토리는 **인터페이스**만 존재합니다. 구현체는 `data/` 레이어에 위치합니다:

```kotlin
// domain/user/repository/UserRepository.kt
interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun registerUser(email: String, password: String): Unit
}
```

### 유즈케이스

유즈케이스는 비즈니스 로직을 담으며 ViewModel에 주입됩니다:

```kotlin
// domain/user/usecase/RegisterUserUseCase.kt
class RegisterUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        userRepository.registerUser(email, password)
    }
}
```

## 의존성

### 빌드 설정

모든 도메인 모듈은 `kuring("kotlin")` 컨벤션 플러그인을 사용합니다:

```gradle
plugins {
    kuring("kotlin")
}

kotlin {
    jvmToolchain(17)
}
```

### 표준 의존성

```gradle
dependencies {
    implementation(projects.data.domain)  # 도메인 모델, 예외

    // 표준 Kotlin/코루틴 (Android 미포함)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
```

**핵심 원칙:** 도메인 모듈은 공유 모델/예외를 위해 `data:domain`에 의존하지만, Android Framework 클래스에는 의존하지 않습니다. 이를 통해 비즈니스 로직의 테스트 가능성과 재사용성을 유지합니다.

## 에이전트 공통 작업

### 새 도메인 모듈 추가

1. 디렉토리 생성: `domain/{newmodule}/`
2. `kuring("kotlin")` 플러그인을 사용하는 `build.gradle.kts` 생성
3. `src/main/java/com/ku_stacks/ku_ring/domain/{newmodule}/repository/`에 리포지토리 인터페이스 생성
4. `src/main/java/com/ku_stacks/ku_ring/domain/{newmodule}/usecase/`에 유즈케이스 생성
5. 리포지토리 구현체를 포함하는 데이터 모듈 생성: `data/{newmodule}/`
6. `app/di/`에 DI 모듈을 추가하여 구현체 제공

### 테스트 실행

```bash
# 모든 도메인 테스트
./gradlew test

# 특정 모듈 테스트
./gradlew :domain:user:test
./gradlew :domain:club:test
```

### 타입 검사

```bash
# 도메인 레이어 컴파일 오류 확인
./gradlew compileDebugKotlin

# 전체 프로젝트 타입 검사
./gradlew :domain:user:build
```

## 주요 패턴

### 의존성 주입 (Hilt)

유즈케이스는 생성자를 통해 ViewModel에 주입됩니다:

```kotlin
// 피처 ViewModel 예시
@HiltViewModel
class LoginViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    // ...
}
```

리포지토리 구현체는 `app/di/` 모듈에서 제공됩니다.

### 코루틴 & Flow

도메인 모듈은 비동기 처리를 위해 `kotlinx.coroutines.core`를 사용합니다:

```kotlin
suspend fun invoke(email: String) {
    withContext(Dispatchers.IO) {
        repository.fetchUser(email)
    }
}
```

옵저버블 데이터에는 `Flow`를 사용합니다:

```kotlin
fun getNotifications(): Flow<List<Notification>> = repository.getNotifications()
```

### 예외 처리

`data:domain`의 도메인 전용 예외를 사용합니다:

```kotlin
try {
    userRepository.registerUser(email, password)
} catch (e: DuplicateEmailException) {
    // 중복 이메일 처리
} catch (e: NetworkException) {
    // 네트워크 오류 처리
}
```

## AI 에이전트 안내

**도메인 모듈을 추가하거나 수정할 때:**

1. **리포지토리 인터페이스는 계약입니다** - 어떤 데이터 접근 메서드가 존재하는지 정의하지만, 실제 HTTP/데이터베이스 로직은 `data/`에 위치합니다.
2. **유즈케이스는 오케스트레이터입니다** - 리포지토리를 호출하고 비즈니스 규칙(유효성 검사, 필터링, 변환)을 적용합니다.
3. **Android 의존성 금지** - 도메인 모듈은 `android.*` 또는 `androidx.*`를 임포트해서는 안 됩니다. 이를 통해 프레임워크에 독립적이고 단위 테스트 가능한 상태를 유지합니다.
4. **Hilt 통합** - 유즈케이스는 ViewModel에 주입되고, 리포지토리는 유즈케이스에 주입됩니다. `new`로 직접 인스턴스화하지 마세요.
5. **코루틴이 표준 비동기 패턴입니다** - `suspend` 함수와 스트림을 위한 `Flow`를 사용하세요.
6. **JUnit5로 테스트** - 도메인 모듈은 JUnit5 테스트 프레임워크를 사용합니다. Android 코드가 없으므로 Robolectric이 필요하지 않습니다.

**피해야 할 일반적인 실수:**

- 인터페이스에 구현 세부사항 포함 (인터페이스는 최소화하고 순수 계약으로 유지).
- 유즈케이스에서 다른 유즈케이스 호출 (대신 리포지토리 메서드를 사용하거나 호출자에서 조합).
- 도메인 코드에 Android 임포트 (data 또는 feature 레이어로 리팩토링).
- DI 하드코딩 (항상 리포지토리와 의존성을 주입).

## 관련 문서

- **데이터 레이어:** [../data/AGENTS.md](../data/AGENTS.md) - 리포지토리 구현체, 데이터 소스
- **피처 레이어:** [../feature/AGENTS.md](../feature/AGENTS.md) - ViewModel에서 유즈케이스 주입
- **앱 DI 설정:** `app/di/` - 리포지토리 구현체 제공
- **네비게이션:** `domain/navigation/` - 공유 라우트 정의
