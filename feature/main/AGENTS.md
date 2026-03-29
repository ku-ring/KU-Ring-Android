<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 피처 Main: AI 에이전트 안내

main 피처 모듈은 로그인 이후의 기본 허브입니다. 공지, 아카이브, 학사 캘린더, 캠퍼스 맵, 동아리, 검색, 설정 등 7개의 주요 기능 영역에 걸친 탭 네비게이션을 제공합니다.

## 모듈 개요

main 모듈은 앱에서 가장 큰 피처 모듈입니다. 단일 탭 인터페이스 아래 여러 서브 화면을 통합합니다.

| 탭 | 패키지 | 목적 | 주요 클래스 |
|-----|---------|---------|------------|
| 공지 | `notice/` | 공지 피드 탐색 | `NoticeListViewModel`, `NoticeScreen`, `NoticeListItem` |
| 아카이브 | `archive/` | 저장/북마크된 공지 | `ArchiveViewModel`, `ArchiveScreen` |
| 캘린더 | `calendar/` | 학사 일정 캘린더 | `CalendarViewModel`, `CalendarScreen` |
| 캠퍼스 맵 | `campusmap/` | 캠퍼스 위치 지도 (Naver Maps) | `CampusMapViewModel`, `CampusMapScreen` |
| 동아리 | `club/` | 동아리 목록 및 관리 | `ClubListViewModel`, `ClubDetailScreen` |
| 검색 | `search/` | 공지/동아리 전문 검색 | `SearchViewModel`, `SearchScreen` |
| 설정 | `setting/` | 앱 설정 및 환경설정 | `SettingViewModel`, `SettingScreen` |

## 모듈 구조

```text
feature/main/
├── build.gradle.kts
├── src/main/java/com/ku_stacks/ku_ring/main/
│   ├── notice/
│   │   ├── compose/
│   │   │   ├── NoticeListScreen.kt
│   │   │   ├── NoticeListViewModel.kt
│   │   │   ├── NoticeListState.kt
│   │   │   ├── NoticeListEvent.kt
│   │   │   ├── NoticeNavigation.kt
│   │   │   ├── components/
│   │   │   │   ├── NoticeListItem.kt
│   │   │   │   ├── NoticeCard.kt
│   │   │   │   └── ...
│   │   │   └── inner_screen/
│   │   │       └── NoticeDetailScreen.kt       # 중첩된 상세 화면 (해당되는 경우)
│   │   └── type/
│   │       └── NoticeType.kt                   # UI enum (SortOrder, FilterType)
│   ├── archive/
│   │   ├── compose/
│   │   │   ├── ArchiveScreen.kt
│   │   │   ├── ArchiveViewModel.kt
│   │   │   ├── ArchiveState.kt
│   │   │   └── components/
│   │   │       └── ArchivedNoticeItem.kt
│   ├── calendar/
│   │   ├── compose/
│   │   │   ├── CalendarScreen.kt
│   │   │   ├── CalendarViewModel.kt
│   │   │   ├── CalendarState.kt
│   │   │   └── component/
│   │   │       ├── calendar/                   # 캘린더 위젯 컴포넌트
│   │   │       │   ├── Calendar.kt
│   │   │       │   ├── Day.kt
│   │   │       │   └── ...
│   │   │       └── EventCard.kt
│   │   ├── model/
│   │   │   └── CalendarEvent.kt               # 로컬 UI 모델
│   │   └── type/
│   │       └── CalendarDisplayMode.kt         # Monthly/Weekly/Daily
│   ├── campusmap/
│   │   ├── compose/
│   │   │   ├── CampusMapScreen.kt
│   │   │   ├── CampusMapViewModel.kt
│   │   │   ├── CampusMapState.kt
│   │   │   └── component/
│   │   │       ├── NaverMapView.kt            # Naver Maps 통합
│   │   │       ├── PlaceMarker.kt
│   │   │       └── PlaceInfoWindow.kt
│   ├── club/
│   │   ├── compose/
│   │   │   ├── ClubListScreen.kt
│   │   │   ├── ClubListViewModel.kt
│   │   │   ├── ClubListState.kt
│   │   │   ├── components/
│   │   │   │   ├── ClubCard.kt
│   │   │   │   ├── ClubCategoryFilter.kt
│   │   │   │   └── ...
│   │   │   └── inner_screen/
│   │   │       ├── ClubDetailScreen.kt
│   │   │       └── ClubSubscribeDialog.kt
│   │   └── type/
│   │       └── ClubCategory.kt
│   ├── search/
│   │   ├── compose/
│   │   │   ├── SearchScreen.kt
│   │   │   ├── SearchViewModel.kt
│   │   │   ├── SearchState.kt
│   │   │   ├── component/
│   │   │   │   ├── SearchBar.kt
│   │   │   │   ├── SearchResultItem.kt
│   │   │   │   └── SearchFilter.kt
│   │   │   └── inner_screen/
│   │   │       └── SearchDetailScreen.kt
│   ├── setting/
│   │   ├── compose/
│   │   │   ├── SettingScreen.kt
│   │   │   ├── SettingViewModel.kt
│   │   │   ├── SettingState.kt
│   │   │   ├── components/
│   │   │   │   ├── SettingItem.kt
│   │   │   │   ├── DarkModeToggle.kt
│   │   │   │   └── VersionInfo.kt
│   │   │   ├── groups/                        # 설정 그룹 (계정, 알림 등)
│   │   │   │   ├── AccountSettingGroup.kt
│   │   │   │   └── NotificationSettingGroup.kt
│   │   │   └── inner_screen/
│   │   │       └── AboutScreen.kt
│   ├── MainScreen.kt                          # 루트 탭 화면
│   └── MainViewModel.kt                       # 탭 간 공유 상태 (필요 시)
├── src/main/res/
│   ├── values/
│   │   ├── strings.xml                        # 모든 UI 문자열
│   │   └── colors.xml                         # 컬러 오버라이드 (있을 경우)
│   └── raw/
│       └── ...                                # Raw 에셋 (지도 설정 등)
└── src/test/java/...
```

## 주요 서브 모듈

### 공지 탭

필터링 및 정렬 기능이 포함된 학사 공지 피드를 표시합니다.

**상태:**
```kotlin
data class NoticeListState(
    val isLoading: Boolean = false,
    val notices: List<Notice> = emptyList(),
    val error: String? = null,
    val sortOrder: SortOrder = SortOrder.LATEST,
    val filters: NoticeFilter = NoticeFilter()
)
```

**이벤트:**
```kotlin
sealed class NoticeListEvent {
    data object Refresh : NoticeListEvent()
    data class Sort(val order: SortOrder) : NoticeListEvent()
    data class Filter(val filter: NoticeFilter) : NoticeListEvent()
}
```

### 아카이브 탭

저장/북마크된 공지를 표시합니다. `domain:notification` 설정과 동기화됩니다.

### 캘린더 탭

월별/주별 뷰로 학사 일정(공휴일, 마감일, 시험 날짜)을 보여줍니다. 날짜 계산에 `kotlinx.datetime`을 사용합니다.

**주요 클래스:**
- `CalendarEvent` - 캘린더 항목의 UI 모델
- `CalendarDisplayMode` - Monthly, Weekly, Daily 뷰
- `CalendarViewModel` - 날짜 네비게이션 및 일정 조회 관리

### 캠퍼스 맵 탭

Naver Maps API를 사용하여 캠퍼스 지도를 렌더링합니다. 카페, 도서관, 강의실 등을 표시합니다.

**의존성:**
```gradle
implementation(libs.bundles.naver.map)  # libs.versions.toml에서 참조
```

**주요 클래스:**
- `NaverMapView` - Naver Map의 Compose 래퍼
- `PlaceMarker` - 지도 위치 마커
- `CampusMapViewModel` - 지도 상태, 위치 선택 관리

### 동아리 탭

구독 관리 기능이 포함된 캠퍼스 동아리/단체 목록을 표시합니다.

**내부 화면:**
- `ClubDetailScreen` - 동아리 프로필 및 구성원
- `ClubSubscribeDialog` - 동아리 가입/탈퇴

### 검색 탭

공지와 동아리에 걸친 전문 검색. `data:search` 리포지토리를 사용합니다.

**상태 구성:**
- 쿼리 텍스트
- 검색 결과 (공지, 동아리)
- 필터 옵션

### 설정 탭

앱 환경설정, 계정 관리, 앱 정보 화면.

**그룹 구성:**
- 계정 (프로필, 비밀번호 변경)
- 알림 (구독 토글)
- 디스플레이 (다크 모드, 언어)
- 앱 정보 (버전, 개인정보처리방침, 문의)

## 빌드 설정

```gradle
import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace
import java.util.Properties

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("retrofit")    # HTTP 요청
    kuringPrimitive("junit5")      # 단위 테스트
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    setNameSpace("main")
    namespace = "com.ku_stacks.ku_ring.main"

    defaultConfig {
        manifestPlaceholders["naverClientId"] =
            properties["naver.client.id"] as String
    }
}

dependencies {
    // 코어 모듈
    implementation(projects.core.util)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.composeLocals)
    implementation(projects.core.firebaseAnalytics)

    // 데이터 레이어
    implementation(projects.data.domain)
    implementation(projects.data.department)
    implementation(projects.data.notice)
    implementation(projects.data.staff)
    implementation(projects.data.search)

    // 도메인 레이어
    implementation(projects.domain.user)
    implementation(projects.domain.academicevent)
    implementation(projects.domain.place)
    implementation(projects.domain.navigation)

    // 라이브러리
    implementation(libs.bundles.compose.interop)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.immutable)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.play.services.auth)
    implementation(libs.bundles.paging)
    implementation(libs.shimmer)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.bundles.naver.map)
}
```

## 에이전트 공통 작업

### 테스트 실행

```bash
# main 모듈 테스트
./gradlew :feature:main:testDebugUnitTest

# 특정 서브 피처 테스트
./gradlew :feature:main:testDebugUnitTest --tests "*.notice.*"
```

### 빌드

```bash
# main 모듈 빌드
./gradlew :feature:main:build

# 컴파일 오류 확인
./gradlew :feature:main:compileDebugKotlin
```

### 새 공지 타입 또는 필터 추가

1. `notice/type/NoticeType.kt`에 enum 추가
2. 새 필터/정렬을 처리하도록 `NoticeListViewModel` 업데이트
3. 필요 시 `NoticeListState` 업데이트
4. `testDebugUnitTest`로 테스트

### 캘린더 디스플레이 수정

1. `calendar/type/`의 `CalendarDisplayMode` 업데이트
2. `CalendarScreen` 컴포저블 수정
3. `CalendarViewModel` 날짜 계산 업데이트
4. Android Studio에서 Preview 테스트

### 지도 기능 추가

1. `NaverMapView` 컴포저블 확장
2. `PlaceMarker` 렌더링 로직 업데이트
3. 새 장소 타입을 조회하도록 `CampusMapViewModel` 수정
4. 지도 API 키를 사용하여 Android 기기/에뮬레이터에서 테스트

## 주요 패턴

### 탭 공유 네비게이션

메인 화면은 `MainViewModel` 또는 로컬 `mutableIntState`로 탭 상태를 관리합니다:

```kotlin
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    var selectedTab by remember { mutableIntState(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NoticeTab(selected = selectedTab == 0, onClick = { selectedTab = 0 })
                ArchiveTab(selected = selectedTab == 1, onClick = { selectedTab = 1 })
                // ... 추가 탭
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> NoticeListScreen()
            1 -> ArchiveScreen()
            // ... 추가 화면
        }
    }
}
```

### 로딩 & 오류 처리

모든 서브 화면은 로딩/오류 상태가 포함된 MVVM 패턴을 따릅니다:

```kotlin
when {
    state.isLoading -> LoadingContent()
    state.error != null -> ErrorContent(error = state.error)
    else -> ContentScreen(state = state, onEvent = viewModel::onEvent)
}
```

### 날짜 처리

캘린더는 `java.time` 대신 `kotlinx.datetime`을 사용합니다:

```kotlin
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock

val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
val month = today.monthNumber
```

### 지도 통합

Naver Maps는 캠퍼스 맵 탭이 활성화될 때만 지연 로딩됩니다:

```kotlin
if (selectedTab == 3) {
    CampusMapScreen()  // Naver SDK가 필요 시 초기화됨
}
```

## AI 에이전트 안내

**main 모듈을 수정할 때:**

1. **각 탭은 독립적입니다** - 다른 탭에 영향 없이 한 탭을 수정하세요. 서브 화면은 공통 패턴을 공유하지만 독립된 ViewModel과 상태를 가집니다.
2. **MainScreen이 탭을 조율합니다** - 탭 선택, 지연 로딩, 네비게이션을 관리합니다. 이 로직을 서브 화면으로 옮기지 마세요.
3. **공유 리소스** - 색상, 타이포그래피, 간격은 `core:designsystem`에서 제공됩니다.
4. **Naver Maps는 API 키가 필요합니다** - `local.properties`에 `naver.client.id`로 설정합니다. 테스트에서는 목(mock)으로 처리해야 합니다.
5. **대형 모듈 = 많은 의존성** - 새 라이브러리를 추가하기 전에 코어 모듈에서 이미 제공하는지 확인하세요.
6. **Preview 친화적 컴포넌트** - 서브 화면 컴포넌트는 독립적으로 미리보기 및 테스트가 가능하도록 설계하세요.

**피해야 할 일반적인 실수:**

- `MainScreen`에 비즈니스 로직 포함 (탭에 무관하게 유지하고, 서브 ViewModel로 이동).
- `kotlinx.datetime` 대신 하드코딩된 날짜 사용.
- Naver Maps 지연 로딩 누락 (메모리/성능 문제 발생).
- UI 레이어에서 캘린더 날짜 계산 (ViewModel 또는 `model/`에 위치).
- 탭 상태를 MainViewModel과 혼용 (탭 선택에는 로컬 상태, 공유 로직에는 ViewModel 사용).

## 관련 문서

- **도메인 레이어:** [../../domain/AGENTS.md](../../domain/AGENTS.md) - 모든 피처의 유즈케이스
- **피처 레이어:** [../AGENTS.md](../AGENTS.md) - 일반 피처 모듈 패턴
- **디자인 시스템:** `core/designsystem/` - 색상, 타이포그래피
- **네비게이션:** `domain/navigation/` - 피처 간 공유 라우트 정의
- **Auth 피처:** [../auth/AGENTS.md](../auth/AGENTS.md) - main 이전의 진입점
