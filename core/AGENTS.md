<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# core 모듈 안내

KU-Ring Android 프로젝트의 공유 라이브러리 레이어입니다. core 모듈은 모든 기능(feature), 데이터(data), 도메인(domain) 모듈이 의존하므로, 이 곳의 변경은 전체 코드베이스에 광범위한 영향을 미칩니다.

## 모듈 목적

core 레이어는 애플리케이션의 모든 레이어에서 사용되는 기반 유틸리티, UI 컴포넌트, 디자인 시스템, Firebase 통합 기능을 제공합니다.

## 모듈 구조

### 11개 공유 모듈

| 모듈 | 역할 |
|--------|---------|
| **util/** | 공통 유틸리티: 날짜 포맷, 확장 함수, 알림 매니저, 결과 타입, Hilt 디스패처 모듈 |
| **compose-util/** | Compose 전용 유틸리티: 키보드 상태 처리, 타이머 유틸리티 |
| **ui/** | 공유 Compose UI 컴포넌트: 동아리, 댓글, 학과, 공지사항 목록 아이템, 미리보기 데이터 프로바이더 |
| **designsystem/** | 디자인 시스템: KuringTheme, 색상, 타이포그래피, 재사용 컴포넌트 (버튼, 텍스트 필드, 탑바, 인디케이터, 드래그앤드롭, 프로그레스) |
| **preferences/** | Hilt DI를 적용한 SharedPreferences 래퍼 (앱 설정 및 사용자 환경설정) |
| **work/** | 백그라운드 작업용 WorkManager 스케줄러: 재참여 알림, 학사 일정, 사용자 등록, 공지사항 캐시 초기화 |
| **testUtil/** | 테스트 유틸리티 및 헬퍼: 목(mock) 유틸리티, LiveData 테스트 확장 함수 |
| **firebase-analytics/** | Hilt DI를 적용한 Firebase Analytics 래퍼 (이벤트 추적) |
| **firebase-crashlytics/** | Firebase Crashlytics 통합: Timber 로깅용 CrashlyticsTree |
| **firebase-messaging/** | FCM 통합: KuringMessagingService, 메시지 타입 매핑, 데이터-엔티티 변환기 |
| **compose-locals/** | Hilt DI를 사용한 CompositionLocal 프로바이더 (Compose 서브트리에 값 주입) |

---

## 주요 의존성 및 패턴

### 빌드 컨벤션

모든 core 모듈은 복합 플러그인을 사용합니다:

```kotlin
plugins {
  kuring("compose")  // UI가 없는 모듈은 kuring("feature")
}
```

Android가 아닌 모듈의 경우:
```kotlin
plugins {
  kuring("kotlin-jvm")
}
```

### Hilt 의존성 주입

런타임 코드가 있는 모든 core 모듈은 Hilt 모듈을 제공해야 합니다:

- **모듈은 기능에 인터페이스를 제공**: 컴패니언 오브젝트 또는 전용 `di/` 패키지에서 `@Provides` 사용
- **예시**: `preferences/PreferenceModule`, `firebase-analytics/di/EventAnalyticsModule`, `work/scheduler/WorkSchedulerModule`

### 테스트

모든 모듈은 `src/test/java/`에 단위 테스트를 포함해야 합니다:

```bash
./gradlew :core:util:testDebugUnitTest
./gradlew :core:designsystem:testDebugUnitTest
./gradlew :core:firebase-messaging:testDebugUnitTest
```

### 광범위한 영향 범위

core 모듈 변경은 다음에 영향을 줍니다:
- 30개 이상의 기능 모듈
- 모든 데이터 모듈
- 모든 도메인 모듈
- app 모듈

**core 변경 후에는 반드시 전체 테스트 스위트를 실행하세요:**
```bash
./gradlew test
```

---

## 모듈 상세 설명

### util/

**목적**: 앱 전반에서 사용되는 기반 유틸리티.

**주요 클래스**:
- `DateUtil`, `DateTimeUtil` – 날짜 포맷 및 변환
- `Result<T>` – 성공/실패 처리를 위한 sealed class
- `KuringNotificationManager` – 알림 생성 및 관리
- `DispatcherModule` – IO, Main, Default 디스패처를 제공하는 Hilt 모듈
- `ActivityExtensions`, `Context` 확장 함수 – Android 확장 함수
- `ThrowableUtil` – 예외 처리 유틸리티

**테스트**: `DateUtilTest`, `StringUtilTest`로 포맷 및 파싱 검증.

---

### compose-util/

**목적**: Compose 전용 유틸리티 및 상태 관리.

**주요 클래스**:
- `KuringTimer` – Compose 화면용 컴포저블 타이머
- `KeyboardState` – IME 표시 여부 변화 감지

**사용법**: 키보드 감지 또는 타이머 동작이 필요한 기능 모듈에서 임포트.

---

### ui/

**목적**: 공통 도메인을 위한 공유 Compose UI 컴포넌트.

**구조**:
- `club/` – 동아리 관련 컴포넌트: ClubItemCard, ClubTag, ClubListSortButtonRow, ClubItemColumn
- `comment/` – 댓글 표시 컴포넌트: Comment composable
- `department/` – 학과 목록 아이템 및 필터
- `notice/` – 공지사항 목록 아이템: NoticeItem, LazyPagingNoticeItemColumn
- `preview/` – Compose 미리보기용 데이터 프로바이더 (NoticePreviewData, DepartmentPreviewData 등)

**Debug 소스 셋**: `src/debug/java/`에 디버그 전용 동아리 UI 컴포넌트 포함.

**리소스 값**: `src/main/res/values/strings.xml`에 기능 간 공유 문자열 리소스.

**사용법**: 기능 모듈에서 이 컴포넌트를 임포트하여 UI 일관성 유지.

---

### designsystem/

**목적**: 테마, 색상, 타이포그래피, 재사용 UI 컴포넌트를 포함한 중앙화된 디자인 시스템.

**구조**:
- `kuringtheme/` – 테마 및 스타일링
  - `KuringTheme.kt` – Compose 테마
  - `KuringColors.kt` – 색상 팔레트 (라이트/다크)
  - `KuringTypography.kt` – 텍스트 스타일 및 폰트 설정
  - `values/` – Android 리소스 값 (색상, 테마)
- `components/` – 재사용 가능한 Compose 컴포넌트
  - `dragdrop/` – 드래그앤드롭 인터랙션 컴포넌트
  - `indicator/` – 로딩 및 페이지 인디케이터: HorizontalSlidingIndicator, PagingLoadingIndicator
  - `progress/` – 프로그레스 컴포넌트: CircularProgressBar
  - `textfield/` – 커스텀 텍스트 필드: KuringTextField, SearchTextField
  - `topbar/` – 앱바 변형: LargeTopAppBar
  - 루트: KuringAlertDialog, KuringCallToAction, KuringScrollableTabRow, KuringWebView, NonLazyGrid, DoubleTapBackHandler
- `utils/` – 디자인 시스템 유틸리티
  - `TextExtension.kt` – Text 컴포저블 확장 함수
  - `Gradient.kt` – 그라디언트 유틸리티
  - `NoRippleInteractionSource.kt` – 리플 효과 커스터마이징
- `res/` – Android 리소스
  - `drawable/`, `drawable-*hdpi/`, `drawable-v24/` – 벡터 및 래스터 드로어블 (다중 밀도)
  - `mipmap-*dpi/` – 앱 아이콘 (다중 밀도)
  - `anim/` – 애니메이션 리소스
  - `values/`, `values-night/` – 문자열, 색상, 치수, 테마
  - `raw/` – 원시 리소스 파일

**사용법**: 모든 기능 모듈은 일관된 테마와 컴포넌트를 위해 designsystem에 의존합니다.

---

### preferences/

**목적**: Hilt DI를 적용한 SharedPreferences 래퍼.

**주요 클래스**:
- `PreferenceUtil` – 환경설정 읽기/쓰기 헬퍼 클래스
- `PreferenceModule` – 공유 환경설정 접근을 제공하는 Hilt 모듈

**패턴**:
```kotlin
@Singleton
fun provideSharedPreferences(context: Context): SharedPreferences =
  context.getSharedPreferences("kuring", Context.MODE_PRIVATE)
```

---

### work/

**목적**: WorkManager 기반의 백그라운드 작업 스케줄링.

**주요 클래스**:
- `ReEngagementNotificationWork` – 재참여 알림 스케줄링
- `AcademicEventWork` – 학사 일정 알림 처리
- `RegisterUserWork` – 사용자 등록 백그라운드 작업
- `NoticeCacheClearWorker` – 캐시된 공지사항 데이터 초기화
- `WorkScheduler` 인터페이스 – 스케줄링 추상화
- `ApplicationWorkScheduler` – WorkManager를 사용한 구현체
- `WorkSchedulerModule` – 스케줄러를 제공하는 Hilt 모듈

**테스트**: `./gradlew :core:work:testDebugUnitTest`

---

### testUtil/

**목적**: 공유 테스트 유틸리티 및 목(mock).

**주요 클래스**:
- `MockUtil` – 테스트 목 생성을 위한 팩토리 함수
- `TestingLiveDataExt` – LiveData 테스트용 확장 함수

**사용법**: 다른 모듈은 `testImplementation` 의존성으로 testUtil을 임포트.

---

### firebase-analytics/

**목적**: 이벤트 추적이 포함된 Firebase Analytics 래퍼.

**주요 클래스**:
- `EventAnalytics` – 메인 애널리틱스 인터페이스
- `di/EventAnalyticsModule` – 애널리틱스 인스턴스를 제공하는 Hilt 모듈

**패턴**: ViewModel 또는 Composable에 `EventAnalytics`를 주입하여 사용자 이벤트를 추적합니다.

---

### firebase-crashlytics/

**목적**: 크래시 리포팅을 위한 Firebase Crashlytics 통합.

**주요 클래스**:
- `CrashlyticsTree` – Crashlytics로 로그를 전송하는 Timber 로깅 트리

**설정**: Application의 onCreate에서 `Timber.plant(CrashlyticsTree())`로 설치합니다.

---

### firebase-messaging/

**목적**: 푸시 알림 및 메시지 처리를 위한 FCM 통합.

**주요 클래스**:
- `KuringMessagingService` – 수신 메시지 처리를 위한 FirebaseMessagingService 확장
- `FcmUtil` – FCM 유틸리티
- `di/FirebaseMessageModule` – Hilt DI 모듈
- `mapper/DataToEntity.kt` – FCM 데이터 페이로드를 도메인 엔티티로 매핑
- `type/NotificationType.kt` – 알림 타입 열거형 (공지사항 카테고리 등)

**메시지 흐름**: FCM → KuringMessagingService → DataToEntity 매퍼 → 도메인 모델 → 로컬 저장소/UI 업데이트

**테스트**: `./gradlew :core:firebase-messaging:testDebugUnitTest`

---

### compose-locals/

**목적**: Hilt DI를 통해 Compose 서브트리에 CompositionLocal 값을 제공합니다.

**주요 클래스**:
- `KuringCompositionLocalProvider` – 로컬 값을 제공하는 컴포저블
- `di/CompositionLocalEntryPoint` – 로컬 값 접근을 위한 Hilt 진입점

**패턴**: 기능 화면을 이 프로바이더로 감싸서 테마, 애널리틱스 또는 기타 공유 상태를 주입합니다.

---

## AI 에이전트 안내

### 작업 지침

core 모듈을 변경할 때:

1. **영향 범위 파악**: core 모듈은 30개 이상의 모듈에 영향을 줍니다. 넓은 범위로 테스트하세요.
2. **테스트 업데이트**: `src/test/java/`에 단위 테스트를 항상 추가/수정하세요.
3. **하위 호환성 유지**: API를 변경할 경우 기능 모듈과 협의하세요.
4. **Hilt 패턴 사용**: 구체적인 구현체가 아닌 DI를 통해 인터페이스를 제공하세요.
5. **모듈 집중도 유지**: 모듈당 하나의 책임만 갖도록 하세요 (디자인 시스템 ≠ 유틸리티).

### 테스트 명령어

특정 core 모듈 테스트 실행:
```bash
./gradlew :core:util:testDebugUnitTest
./gradlew :core:designsystem:testDebugUnitTest
./gradlew :core:ui:testDebugUnitTest
./gradlew :core:firebase-messaging:testDebugUnitTest
./gradlew :core:preferences:testDebugUnitTest
./gradlew :core:work:testDebugUnitTest
```

모든 core 모듈 테스트 실행:
```bash
./gradlew :core:testDebugUnitTest
```

전체 테스트 스위트 실행 (통합 이슈 감지):
```bash
./gradlew test
```

### 일반 작업

**새로운 공유 컴포넌트 추가**:
- `core/designsystem/src/main/java/com/ku_stacks/ku_ring/designsystem/components/`에 파일 생성
- Compose 미리보기 추가
- designsystem/AGENTS.md 업데이트

**새로운 Firebase 기능 추가**:
- `core/firebase-{feature}/` 아래에 모듈 생성
- `di/`에 Hilt 모듈 제공
- 이 파일에 문서화

**테마 색상 업데이트**:
- `core/designsystem/src/main/java/com/ku_stacks/ku_ring/designsystem/kuringtheme/KuringColors.kt` 수정
- `core/designsystem/src/main/res/values/colors.xml` 업데이트 (Android 리소스 참조)
- `./gradlew test`로 모든 기능에서 테스트

### 의존성 주의사항

- core 모듈은 기능(feature), 데이터(data), 도메인(domain) 모듈에 의존해서는 안 됩니다
- core 모듈은 다른 core 모듈에 의존할 수 있습니다 (예: ui는 designsystem에 의존)
- 의존성 그래프는 비순환 단방향으로 유지하세요
