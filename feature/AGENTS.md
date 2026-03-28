<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 피처 레이어: AI 에이전트 안내

피처 레이어는 Jetpack Compose와 MVVM 아키텍처로 구축된 모든 UI 화면을 포함합니다. 각 피처 모듈은 ViewModel, Compose 화면, 네비게이션을 갖춘 독립적인 화면 또는 기능 세트입니다.

## 모듈 개요

| 모듈 | 목적 | 주요 화면 |
|--------|---------|------------|
| `main/` | 앱 메인 허브: 공지 목록, 아카이브, 캘린더, 캠퍼스 맵, 동아리, 검색, 설정 | 공지 목록, 아카이브, 캘린더, 캠퍼스 맵, 동아리 목록, 검색, 설정 |
| `auth/` | 인증: 로그인, 회원가입, 로그아웃, 비밀번호 재설정 | 로그인, 회원가입, 비밀번호 재설정 |
| `splash/` | 앱 시작 시 로딩/스플래시 화면 | 스플래시 화면 |
| `onboarding/` | 첫 사용자 온보딩 플로우 | 온보딩 단계 |
| `notice_detail/` | WebView, 댓글, 신고가 포함된 공지 상세 화면 | 상세 화면, 댓글, 신고 다이얼로그 |
| `notification/` | 푸시 알림 목록 화면 | 알림 센터 |
| `kuringbot/` | AI 챗봇 인터페이스 | 챗봇 UI |
| `library/` | 도서관 좌석 현황 확인 | 도서관 검색, 현황 |
| `club/` | 동아리 상세 정보, 온보딩, 구독 관리 | 동아리 상세, 구독 다이얼로그 |
| `feedback/` | 사용자 피드백 제출 폼 | 피드백 폼 |
| `edit_departments/` | 학과 선택 편집기 | 학과 선택기 |
| `edit_subscription/` | 알림 구독 편집기 | 구독 설정 |
| `notion/` | Notion 기반 콘텐츠 뷰어 (WebView 또는 레이아웃 기반) | 콘텐츠 뷰어 |

## 모듈 구조

### 표준 피처 모듈 패턴

각 피처 모듈은 아래 구조를 따릅니다:

```
feature/{module}/
├── build.gradle.kts                              # Convention: kuring("view"), kuring("compose")
├── src/main/java/com/ku_stacks/ku_ring/{module}/
│   ├── compose/
│   │   ├── {Feature}Screen.kt                   # 메인 화면 컴포저블 + 네비게이션
│   │   ├── {Feature}ViewModel.kt                # MVVM ViewModel
│   │   ├── {Feature}State.kt                    # UI 상태 클래스
│   │   ├── {Feature}SideEffect.kt               # 사이드 이펙트 enum (필요 시)
│   │   ├── component/                           # 재사용 가능한 UI 컴포넌트
│   │   │   ├── button/
│   │   │   ├── textfield/
│   │   │   ├── topbar/
│   │   │   └── {ComponentName}.kt
│   │   └── inner_screen/                        # 서브 화면/다이얼로그
│   │       └── {SubScreen}Screen.kt
│   ├── model/                                   # 로컬 UI 모델 (도메인 외부)
│   ├── type/                                    # UI용 enum, sealed class
│   └── state/                                   # UI 상태 관리 클래스
├── src/main/res/
│   ├── values/
│   │   ├── strings.xml                         # 문자열 리소스
│   │   └── colors.xml                          # 컬러 오버라이드 (있을 경우)
│   └── raw/                                     # Raw 파일 (필요 시)
└── src/test/java/...                           # 단위 테스트
```

### ViewModel 패턴

모든 피처 모듈은 Hilt 주입 ViewModel과 MVVM + 단방향 데이터 플로우를 사용합니다:

```kotlin
@HiltViewModel
class MyFeatureViewModel(
    private val useCase1: UseCase1,
    private val useCase2: UseCase2
) : ViewModel() {
    private val _state = MutableStateFlow(MyFeatureState())
    val state: StateFlow<MyFeatureState> = _state.asStateFlow()

    fun onEvent(event: MyFeatureEvent) {
        when (event) {
            is MyFeatureEvent.Click -> handleClick()
            // ...
        }
    }

    private fun handleClick() {
        viewModelScope.launch {
            try {
                val result = useCase1()
                _state.update { it.copy(data = result) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
}
```

### Compose 화면 패턴

화면은 상태를 관찰하고 이벤트를 발행하는 상태 비저장(stateless) 컴포저블입니다:

```kotlin
@Composable
fun MyFeatureScreen(viewModel: MyFeatureViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MyFeatureContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun MyFeatureContent(
    state: MyFeatureState,
    onEvent: (MyFeatureEvent) -> Unit
) {
    Column {
        Button(onClick = { onEvent(MyFeatureEvent.Click) }) {
            Text("Click me")
        }
    }
}
```

### 네비게이션 통합

네비게이션 라우트는 `domain/navigation/`에 정의됩니다. 화면은 앱의 네비게이션 그래프에 등록됩니다.

```kotlin
// domain/navigation/ 에서 라우트 정의
sealed class Route {
    data object MyFeature : Route()
}

// feature/{module}/compose/MyFeatureNavigation.kt
fun NavGraphBuilder.myFeatureNavigation() {
    composable<Route.MyFeature> {
        MyFeatureScreen()
    }
}
```

## 의존성

### 빌드 설정

모든 피처 모듈은 `kuring("view")`와 `kuring("compose")` 컨벤션 플러그인을 사용합니다:

```gradle
import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
}

android {
    setNameSpace("feature.{module}")
}
```

특정 피처를 위한 선택적 플러그인:

```gradle
kuringPrimitive("retrofit")      # HTTP 호출이 필요한 경우
kuringPrimitive("junit5")         # 단위 테스트용
kuringPrimitive("room")           # 로컬 데이터베이스 사용 시
```

### 표준 의존성

```gradle
dependencies {
    // 코어 모듈
    implementation(projects.core.util)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.composeUtil)
    implementation(projects.core.composeLocals)
    implementation(projects.core.firebaseAnalytics)

    // 도메인 레이어 (유즈케이스 및 엔티티)
    implementation(projects.domain.user)
    implementation(projects.domain.club)
    implementation(projects.domain.navigation)

    // 데이터 레이어 (리포지토리)
    implementation(projects.data.domain)
    implementation(projects.data.notice)

    // Compose 의존성 (컨벤션 플러그인에서 관리)
    implementation(libs.bundles.compose.interop)
}
```

## 에이전트 공통 작업

### 새 피처 모듈 추가

1. 디렉토리 생성: `feature/{newfeature}/`
2. `build.gradle.kts` 생성:
   ```gradle
   import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

   plugins {
       kuring("view")
       kuring("compose")
   }

   android {
       setNameSpace("feature.newfeature")
   }

   dependencies {
       implementation(projects.core.util)
       implementation(projects.core.designsystem)
       implementation(projects.core.composeUtil)
       // 필요에 따라 domain/data 의존성 추가
   }
   ```
3. `src/main/java/com/ku_stacks/ku_ring/{newfeature}/compose/{Feature}Screen.kt` 생성
4. `src/main/java/com/ku_stacks/ku_ring/{newfeature}/compose/{Feature}ViewModel.kt` 생성
5. `app/`의 네비게이션 그래프 또는 lazy compose 네비게이션에 등록
6. `settings.gradle.kts`에 새 모듈 추가

### 테스트 실행

```bash
# 모든 피처 테스트
./gradlew :feature:auth:testDebugUnitTest

# 특정 피처 모듈
./gradlew :feature:club:testDebugUnitTest

# 전체 모듈 테스트
./gradlew test
```

### 피처 빌드

```bash
# 단일 피처 모듈 빌드
./gradlew :feature:main:build

# 빌드 및 오류 확인
./gradlew :feature:auth:compileDebugKotlin
```

### Compose 화면 미리보기

Android Studio의 Compose Preview 또는 `@Preview` 어노테이션 사용:

```kotlin
@Preview
@Composable
fun MyFeatureScreenPreview() {
    MyFeatureContent(
        state = MyFeatureState(),
        onEvent = {}
    )
}
```

## 주요 패턴

### 상태 관리

UI 상태는 `StateFlow`에 저장되며 `collectAsStateWithLifecycle()`로 수집됩니다:

```kotlin
data class MyFeatureState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null
)

val state by viewModel.state.collectAsStateWithLifecycle()
```

### 이벤트 처리

이벤트는 sealed class 또는 enum으로 정의합니다:

```kotlin
sealed class MyFeatureEvent {
    data object Click : MyFeatureEvent()
    data class Input(val text: String) : MyFeatureEvent()
}
```

### 사이드 이펙트

네비게이션, 토스트 등 일회성 동작을 위한 선택적 사이드 이펙트:

```kotlin
sealed class MyFeatureSideEffect {
    data object NavigateBack : MyFeatureSideEffect()
    data class ShowToast(val message: String) : MyFeatureSideEffect()
}

// ViewModel에서
private val _sideEffect = Channel<MyFeatureSideEffect>()
val sideEffect = _sideEffect.receiveAsFlow()

// 사이드 이펙트 발행
_sideEffect.send(MyFeatureSideEffect.ShowToast("Success!"))
```

### 재사용 가능한 컴포넌트

공유 UI 컴포넌트는 `compose/component/`에 배치합니다:

```kotlin
@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Text(text)
    }
}
```

### 디자인 시스템 통합

색상, 타이포그래피, 형태에는 `core:designsystem`을 사용합니다:

```kotlin
import com.ku_stacks.ku_ring.core.designsystem.color.KuRingColors
import com.ku_stacks.ku_ring.core.designsystem.typography.KuRingTypography

Text(
    text = "Hello",
    color = KuRingColors.primary,
    style = KuRingTypography.headlineSmall
)
```

## AI 에이전트 안내

**피처 모듈을 추가하거나 수정할 때:**

1. **ViewModel은 MVVM 컨트롤러입니다** - 상태 보유, 이벤트 처리, 유즈케이스 호출을 담당합니다. ViewModel에 UI 로직(Compose)을 넣지 마세요.
2. **화면은 상태 비저장 컴포저블입니다** - ViewModel에서 상태를 읽고 `onEvent()` 콜백을 호출합니다. 비즈니스 로직은 없습니다.
3. **유즈케이스는 ViewModel에 주입됩니다** - 리포지토리와 유즈케이스는 `domain/`과 `data/` 레이어에서 제공됩니다.
4. **Hilt가 의존성을 제공합니다** - `@HiltViewModel`과 `hiltViewModel()` 컴포저블 함수를 사용하세요.
5. **StateFlow + collectAsStateWithLifecycle()** - Compose에서 상태를 수집하는 표준 패턴입니다.
6. **디자인 시스템 색상/타이포그래피** - 하드코딩된 색상 대신 `core:designsystem` 토큰을 사용하세요.
7. **네비게이션 라우트** - 피처 모듈이 아닌 `domain/navigation/`에 정의됩니다.

**피해야 할 일반적인 실수:**

- Compose 화면에 비즈니스 로직 포함 (ViewModel로 이동).
- 색상 또는 폰트 크기 하드코딩 (디자인 시스템 사용).
- `MyViewModel()`로 ViewModel 직접 인스턴스화 (`hiltViewModel()` 사용).
- 상태/이벤트 클래스 누락 (이벤트에는 항상 sealed class 정의).
- Preview 컴포저블 미테스트 (`@Preview` 사용 후 Android Studio에서 확인).
- 잘못된 위치에 네비게이션 임포트 (라우트는 `domain/navigation/`에 정의).

## 관련 문서

- **도메인 레이어:** [../domain/AGENTS.md](../domain/AGENTS.md) - 유즈케이스 및 리포지토리 인터페이스
- **데이터 레이어:** [../data/AGENTS.md](../data/AGENTS.md) - 리포지토리 구현체
- **코어 모듈:** [../core/AGENTS.md](../core/AGENTS.md) - 공유 UI 컴포넌트 및 유틸리티
- **네비게이션:** `domain/navigation/` - 라우트 정의
- **디자인 시스템:** `core/designsystem/` - 색상, 타이포그래피, 형태
- **Main 피처:** [main/AGENTS.md](main/AGENTS.md) - 가장 큰 피처 모듈의 상세 안내
- **Auth 피처:** [auth/AGENTS.md](auth/AGENTS.md) - 인증 플로우 상세 정보
