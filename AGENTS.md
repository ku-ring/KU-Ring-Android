<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# KU-Ring Android: AI 에이전트 안내

**KU-Ring**은 건국대학교 학생을 위한 알림 앱입니다. Jetpack Compose, Hilt(의존성 주입), Kotlin Coroutines를 활용한 Clean Architecture 기반의 멀티 모듈 Android 프로젝트입니다.

## 빠른 참조

### 코드베이스 개요

- **루트 파일**: `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, `gradlew`, `LICENSE`, `README.md`, `renovate.json`
- **아키텍처**: Feature → Domain → Data, Core는 모든 레이어에서 공유
- **언어**: Kotlin, UI는 Jetpack Compose 사용
- **빌드 시스템**: `build-logic/`의 컨벤션 플러그인, 커스텀 DSL(`kuring()`, `kuringPrimitive()`) 사용
- **모듈 수**: 4개 주요 레이어로 구성된 40개 이상의 모듈

### 기술 스택

**핵심 프레임워크**
- Jetpack Compose (MVVM + 단방향 데이터 흐름 UI 프레임워크)
- Hilt (의존성 주입)
- Kotlin Coroutines + Flow (비동기 처리)

**Android Jetpack**
- ViewModel, Room (로컬 데이터베이스), Lifecycle, Paging3, WorkManager, Navigation Compose
- Startup (앱 초기화), Compose Locals (서브트리에 값 제공)

**네트워킹 & 저장소**
- Ktor 또는 Retrofit2 (HTTP 요청)
- Gson (JSON 직렬화)
- Room (로컬 SQLite 데이터베이스)

**Firebase & 서비스**
- Analytics, Crashlytics, Cloud Messaging
- Naver Maps API
- Sendbird (채팅 SDK)
- Google Play Services (Auth, Location, OSS Licenses)

**테스팅**
- JUnit5 (테스트 프레임워크)
- Robolectric (Android 프레임워크 모킹)
- Mockito (모킹 라이브러리)
- Espresso + Navigation Testing (계측 테스트)

---

## 모듈 구조

### 루트 레벨

```
KU-Ring-Android/
├── app/                    # 메인 애플리케이션 모듈 (진입점)
├── core/                   # 모든 레이어에서 공유하는 라이브러리
├── data/                   # 데이터 레이어 (레포지토리, 데이터 소스)
├── domain/                 # 비즈니스 로직 (유스케이스, 엔티티)
├── feature/                # UI 화면 (MVVM ViewModel + Compose)
├── build-logic/            # Gradle 컨벤션 플러그인
└── gradle/                 # Gradle 래퍼 및 버전 카탈로그 (libs.versions.toml)
```

### Core 모듈 (`/core`)

공유 유틸리티 및 기반 컴포넌트:

| 모듈 | 용도 |
|--------|---------|
| `core:util` | 유틸리티 및 확장 함수 |
| `core:compose-util` | Compose 헬퍼 함수 |
| `core:ui` | 공통 Compose 컴포넌트 |
| `core:designsystem` | 디자인 토큰, 타이포그래피, 색상 |
| `core:preferences` | SharedPreferences 래퍼 |
| `core:work` | WorkManager 설정 |
| `core:testUtil` | 테스트 유틸리티 및 픽스처 |
| `core:firebase-analytics` | Firebase Analytics 연동 |
| `core:firebase-crashlytics` | Firebase Crashlytics 연동 |
| `core:firebase-messaging` | Firebase Cloud Messaging |
| `core:compose-locals` | Provide/CompositionLocal 설정 |

### Data 모듈 (`/data`)

레포지토리 및 원격/로컬 데이터 소스를 포함한 데이터 레이어:

| 모듈 | 용도 |
|--------|---------|
| `data:domain` | 도메인 모델, 예외, 기본 타입 |
| `data:domain:testUtils` | 도메인 모델 테스트 픽스처 |
| `data:notice` | 공지 데이터 소스 및 레포지토리 |
| `data:notice:test` | 공지 테스트 유틸리티 |
| `data:noticecomment` | 공지 댓글 데이터 |
| `data:notification` | 알림 데이터 |
| `data:academicevent` | 학사 일정 데이터 |
| `data:ai` | AI/봇 연동 데이터 |
| `data:user` | 사용자 프로필 데이터 |
| `data:staff` | 교직원 목록 데이터 |
| `data:department` | 학과 정보 데이터 |
| `data:department:test` | 학과 테스트 유틸리티 |
| `data:library` | 도서관 데이터 |
| `data:local` | 로컬 데이터베이스 (Room) |
| `data:local:test` | 로컬 데이터베이스 테스트 유틸리티 |
| `data:place` | 장소/위치 데이터 |
| `data:remote` | 원격 HTTP 데이터 소스 (Ktor/Retrofit) |
| `data:report` | 제보/피드백 데이터 |
| `data:space` | 공간/장소 데이터 |
| `data:search` | 검색 기능 |
| `data:verification` | 인증 데이터 |
| `data:club` | 동아리 정보 데이터 |

### Domain 모듈 (`/domain`)

비즈니스 로직 및 유스케이스:

| 모듈 | 용도 |
|--------|---------|
| `domain:club` | 동아리 유스케이스 |
| `domain:user` | 사용자 유스케이스 |
| `domain:noticecomment` | 공지 댓글 유스케이스 |
| `domain:notification` | 알림 유스케이스 |
| `domain:report` | 제보 유스케이스 |
| `domain:academicevent` | 학사 일정 유스케이스 |
| `domain:place` | 장소 유스케이스 |
| `domain:navigation` | 내비게이션 상태 관리 |

### Feature 모듈 (`/feature`)

ViewModel 및 Compose를 포함한 UI 화면:

| 모듈 | 용도 |
|--------|---------|
| `feature:auth` | 로그인/인증 화면 |
| `feature:club` | 동아리 목록 및 상세 |
| `feature:edit_subscription` | 알림 구독 관리 |
| `feature:feedback` | 피드백 입력 화면 |
| `feature:library` | 도서관 검색 및 정보 |
| `feature:notice_detail` | 공지 상세 보기 |
| `feature:notion` | Notion 연동 |
| `feature:onboarding` | 최초 사용자 설정 |
| `feature:splash` | 스플래시/로딩 화면 |
| `feature:main` | 메인 탭 내비게이션 화면 |
| `feature:kuringbot` | AI 챗봇 인터페이스 |
| `feature:edit_departments` | 학과 설정 |
| `feature:notification` | 알림 센터 |

### Build Logic (`/build-logic`)

모듈 설정을 표준화하는 Gradle 컨벤션 플러그인:

**컨벤션 플러그인** (프리미티브의 조합):
- `ApplicationPlugin` - app 모듈 설정
- `FeaturePlugin` - Compose + Hilt를 포함한 feature 모듈 설정
- `ViewBasedFeaturePlugin` - Compose 없는 레거시 feature 모듈
- `ComposePlugin` - Jetpack Compose 설정

**프리미티브 플러그인** (단일 책임):
- `KotlinPlugin` - Kotlin 컴파일러 옵션
- `CommonAndroidPlugin` - Android SDK, 빌드 툴, 매니페스트
- `ComposePlugin` - Compose 컴파일러 및 의존성
- `HiltPlugin` - Hilt DI 설정
- `TestPlugin` - JUnit5 + Robolectric
- `AndroidTestPlugin` - Espresso + 계측 테스트
- `RoomPlugin` - Room 데이터베이스
- `FirebasePlugin` - Firebase analytics/crashlytics
- `RetrofitPlugin` - Retrofit2 HTTP
- `KtorPlugin` - Ktor HTTP 클라이언트
- `OkHttpPlugin` - OkHttp3 with logging
- `JUnit5TestPlugin` - JUnit5 프레임워크

**DSL 헬퍼** (`buildlogic/dsl/`):
- `AndroidGradleDsl.kt` - Android 설정 헬퍼
- `ProjectDsl.kt` - 프로젝트 레벨 DSL
- `GradleDsl.kt` - 일반 Gradle 유틸리티

### Gradle & 버전 (`/gradle`)

- **libs.versions.toml** - 중앙 집중식 버전 카탈로그
  - AGP: 9.1.0 (Android Gradle Plugin)
  - Kotlin: 2.3.20
  - Compose BOM: 2026.03.01
  - minSdk: 24, targetSdk: 35, compileSdk: 36
  - appVersion: 2.4.3, versionCode: 20404

---

## 주요 패턴 & 컨벤션

### 의존성 주입 (Hilt)

- 진입점: `@HiltAndroidApp`이 적용된 `KuRingApplication.kt`
- `app/di/`의 DI 모듈이 데이터 소스와 레포지토리를 연결
- Feature는 생성자를 통해 유스케이스와 레포지토리를 주입받음

### 모듈 의존성

`settings.gradle.kts`에서 활성화된 **타입 세이프 프로젝트 접근자** (Gradle 기능) 사용:

```kotlin
// 대신: implementation("com.example:notice:1.0")
// 이렇게 사용:
implementation(projects.data.notice)
implementation(projects.feature.club)
```

### 빌드 타입 변형

Debug와 Release 빌드의 차이점:
- 앱 이름 (App Name Debug vs KU-Ring)
- 아이콘 (개발용 런처 아이콘 vs 릴리즈 아이콘)
- Application ID 접미사 (디버그 빌드는 `.debug`)

릴리즈 빌드 서명 설정:
- 키스토어 파일: `app/signing/ku_ring_keystore.jks`
- 설정 파일: `app/signing/keystore.properties`

### Room 데이터베이스

- 엔티티 정의는 `data:local` 모듈에 위치
- 버전 추적을 위한 스키마 스냅샷은 `app/schemas/`에 저장
- `app/build.gradle.kts`에서 스키마 위치 설정:
  ```
  room.schemaLocation = "$projectDir/schemas"
  ```

### 내비게이션

- 내비게이션 상태는 `domain:navigation`에서 관리
- 라우터 구현: `app/navigator/KuringNavigatorImpl.kt`
- 타입 세이프 인자를 사용한 Compose Navigation 사용

### 앱 초기화

- 앱 시작 초기화는 `app/initializer/`에 위치
- 예시: `TimberInitializer.kt` (로깅 설정)
- 앱 실행 시 `androidx.startup`을 통해 트리거됨

---

## 에이전트를 위한 일반 작업

### 테스트 실행

```bash
# 단위 테스트 (debug)
./gradlew :app:testDebugUnitTest

# 모든 단위 테스트
./gradlew test

# 계측 테스트 (Android 기기/에뮬레이터)
./gradlew :app:connectedAndroidTest

# 특정 모듈
./gradlew :feature:club:test
```

### 빌드

```bash
# Debug 빌드
./gradlew assembleDebug

# Release 빌드 (서명 설정 필요)
./gradlew assembleRelease

# 특정 모듈 빌드
./gradlew :data:notice:build
```

### 의존성 관리

- 버전 카탈로그: `gradle/libs.versions.toml`
- Renovate가 의존성을 자동으로 업데이트 (`renovate.json` 참고)
- 알려진 이슈:
  - `play-services-location:21.0.2` (위치 서비스)
  - `okhttp3:logging-interceptor` (HTTP 로깅)
  - 일부 모듈에 명시적 버전 누락; 버전 카탈로그를 통해 해결

### 모듈 의존성 그래프 생성

```bash
# macOS
brew install graphviz
./gradlew projectDependencyGraph

# Windows
# 설치: https://graphviz.org/download/
gradlew projectDependencyGraph

# 출력: project.dot.png
```

---

## 변경 전 확인 사항

**먼저 코드를 읽으세요.** Hilt 모듈, 내비게이션, Room 스키마에 대한 잘못된 가정은 광범위한 오류를 유발할 수 있습니다.

**주요 변경 전 이해해야 할 파일:**
- `app/di/` - DI 모듈 정의
- `app/navigator/KuringNavigatorImpl.kt` - 내비게이션 라우팅
- `domain:navigation` - 내비게이션 상태 관리
- `data:local` - Room 데이터베이스 스키마
- `app/schemas/` - Room 마이그레이션 이력

**컨벤션 플러그인 변경은 영향 범위가 큽니다:**
- 테스트: `./gradlew :feature:main:build` (또는 임의의 feature 모듈)
- DI 정상 작동 검증: `./gradlew :app:build`

**Feature 모듈 변경:**
- 올바른 Hilt 설정에 의존
- Compose 및 UI 변경 시 designsystem의 색상/타이포그래피 업데이트 필요할 수 있음
- 테스트: `./gradlew :feature:club:test` (또는 해당 feature)

---

## 문제 해결

### 빌드 실패

1. **Gradle 동기화 문제**
   - `.gradle/`와 `build/` 폴더 삭제
   - IDE 캐시 무효화
   - `./gradlew clean` 실행

2. **Hilt 문제**
   - `app/di/`의 DI 모듈은 `KuRingApplication.kt`에 포함되거나 `@HiltAndroidApp`에 의해 자동 감지되어야 함
   - 모듈 간 순환 의존성 확인

3. **Compose 컴파일**
   - `libs.versions.toml`의 `compose-compiler` 버전이 Kotlin 버전과 일치하는지 확인
   - 현재: Compose 2026.03.01, Compiler 1.4.8

4. **Room 스키마 변경**
   - 새 엔티티는 `app/schemas/`에 스키마 스냅샷 필요
   - `./gradlew :app:build` 실행하여 생성

### 일반 오류

| 오류 | 원인 | 해결 방법 |
|-------|-------|-----|
| `Unable to find Hilt binding for` | DI 모듈이 해당 타입을 제공하지 않음 | `app/di/` 모듈 확인 |
| `Cannot find symbol @HiltViewModel` | Hilt 플러그인 누락 | `build-logic` 컨벤션 플러그인 확인 |
| `Compose version mismatch` | Kotlin 또는 컴파일러 버전 충돌 | `libs.versions.toml` 업데이트 |
| `Room schema export failed` | 스키마 디렉토리 없음 | `./gradlew clean build` 실행 |

---

## 연락처 & 리소스

- **이메일**: kuring.korea@gmail.com
- **인스타그램**: @kuring.konkuk
- **GitHub**: [KU-Stacks/KU-Ring-Android](https://github.com/KU-Stacks/KU-Ring-Android)
- **프로젝트 그래프**: 레포 루트의 `project.dot.png` 참고
