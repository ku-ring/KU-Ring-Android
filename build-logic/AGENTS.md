<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# build-logic 모듈 안내

이 모듈은 KU-Ring Android 프로젝트의 Gradle 컨벤션 플러그인 모듈입니다. 다른 모든 모듈에서 공통으로 사용하는 재사용 가능한 빌드 설정을 정의합니다.

## 모듈 목적

build-logic 모듈은 두 계층의 플러그인으로 구성됩니다:

1. **프리미티브 플러그인(Primitive plugins)** - 하나의 프레임워크 또는 라이브러리를 적용하는 단일 책임 플러그인
2. **컨벤션 플러그인(Convention plugins)** - 여러 프리미티브를 묶어 도메인별 설정을 제공하는 고수준 복합 플러그인

모듈은 버전 카탈로그에서 플러그인을 로드하는 DSL 함수 `kuring()`과 `kuringPrimitive()`를 통해 컨벤션 플러그인을 사용합니다.

## 모듈 구조

### 컨벤션 플러그인

여러 프리미티브를 조합하는 고수준 플러그인입니다. 새 모듈을 생성할 때 사용하세요.

#### ApplicationPlugin
- **목적**: 앱 모듈 전체 설정 (Android application)
- **적용 항목**: KotlinPlugin, CommonAndroidPlugin, HiltPlugin, ComposePlugin
- **추가 항목**: 버전 코드/이름, 릴리즈 빌드 타입, ProGuard 규칙, Fragment 및 ConstraintLayout 의존성
- **사용법**: 앱 모듈 `build.gradle.kts`에서 `kuring("application")`
- **AGP 9.0 참고**: 블록 스타일 람다 대신 `androidExtension()` 헬퍼와 `.apply {}` 패턴 사용

#### FeaturePlugin
- **목적**: 피처 모듈 설정 (DI 및 내비게이션이 포함된 Android 라이브러리)
- **적용 항목**: KotlinPlugin, CommonAndroidPlugin, HiltPlugin
- **추가 항목**: Consumer ProGuard 규칙, 릴리즈 빌드 타입
- **사용법**: 피처 모듈 `build.gradle.kts`에서 `kuring("feature")`
- **주요 용도**: Hilt 주입과 ModuleNavGraph 설정이 필요한 피처 모듈

#### ComposePlugin (컨벤션)
- **목적**: Compose 기반 라이브러리 모듈 설정
- **적용 항목**: FeaturePlugin, ComposePlugin (프리미티브)
- **사용법**: Compose 라이브러리 모듈에서 `kuring("compose")`
- **주요 용도**: Jetpack Compose UI를 사용하는 피처 모듈

#### ViewBasedFeaturePlugin
- **목적**: 레거시 뷰 기반 피처 모듈 설정
- **적용 항목**: FeaturePlugin
- **추가 항목**: View binding, Fragment 및 ConstraintLayout 의존성
- **사용법**: 레거시 뷰 모듈에서 `kuring("view-based-feature")`
- **참고**: 새 모듈에는 ComposePlugin 사용을 권장

#### KotlinJvmPlugin
- **목적**: 순수 Kotlin JVM 모듈 설정 (Android 제외)
- **사용법**: Kotlin 전용 모듈에서 `kuring("kotlin-jvm")`
- **주요 용도**: 공유 Kotlin 비즈니스 로직 또는 유틸리티 라이브러리

### 프리미티브 플러그인

하나의 프레임워크 또는 라이브러리를 적용하는 세분화된 플러그인입니다. 세밀한 제어가 필요할 때 사용하세요.

#### CommonAndroidPlugin
- **목적**: 모든 Android 모듈에 적용되는 기본 Android 설정
- **설정 항목**:
  - 테스트 계측 러너: `androidx.test.runner.AndroidJUnitRunner`
  - 빌드 기능: buildConfig 활성화
- **추가 의존성**: AndroidX (appcompat, core-ktx, lifecycle, activity), Material Design, Timber
- **사용법**: `kuringPrimitive("common-android")`

#### KotlinPlugin
- **목적**: Kotlin 컴파일러 및 코루틴 설정
- **설정 항목**:
  - JVM 타겟: Java 17 (`JvmTarget.JVM_17`)
  - 소스/타겟 호환성: Java 17
  - Kotlin 직렬화 플러그인
- **추가 의존성**: kotlinx-coroutines (core, android, reactive), kotlinx-serialization
- **사용법**: `kuringPrimitive("kotlin")`

#### ComposePlugin (프리미티브)
- **목적**: Jetpack Compose 컴파일러 설정
- **추가 항목**: Compose 컴파일러 의존성 및 설정
- **사용법**: `kuringPrimitive("compose")`

#### HiltPlugin
- **목적**: Hilt (Dagger) 의존성 주입 설정
- **적용 항목**: Google KSP, Dagger Hilt Android 플러그인
- **추가 의존성**:
  - hilt-android, hilt-navigation-compose
  - hilt-compiler (코드 생성)
  - hilt-android-testing (테스트용)
- **프로세서**: KSP를 통한 androidx-hilt-compiler, hilt-compiler
- **사용법**: `kuringPrimitive("hilt")`

#### RoomPlugin
- **목적**: Room 데이터베이스 설정
- **추가 의존성**: Room 런타임, 컴파일러, KTX 확장
- **사용법**: `kuringPrimitive("room")`

#### KtorPlugin
- **목적**: Ktor 클라이언트 HTTP 설정
- **추가 의존성**: Ktor 클라이언트 코어, 엔진 및 플러그인
- **사용법**: `kuringPrimitive("ktor")`

#### OkHttpPlugin
- **목적**: OkHttp HTTP 클라이언트 설정
- **추가 의존성**: OkHttp, logging-interceptor
- **사용법**: `kuringPrimitive("okhttp")`

#### RetrofitPlugin
- **목적**: Retrofit HTTP API 설정
- **추가 의존성**: Retrofit, 직렬화용 컨버터
- **사용법**: `kuringPrimitive("retrofit")`

#### FirebasePlugin
- **목적**: Firebase 서비스 설정
- **추가 의존성**: Firebase Analytics, Cloud Messaging, Crashlytics
- **사용법**: `kuringPrimitive("firebase")`

#### TestPlugin
- **목적**: JUnit 4 단위 테스트 설정
- **추가 의존성**: JUnit 4, Mockito, 테스트 유틸리티
- **사용법**: `kuringPrimitive("test")`

#### JUnit5TestPlugin
- **목적**: JUnit 5 (Jupiter) 단위 테스트 설정
- **추가 의존성**: JUnit 5 엔진, 어설션 라이브러리, Mockito 5
- **사용법**: `kuringPrimitive("junit5")`

#### AndroidTestPlugin
- **목적**: 계측 테스트 설정 (Espresso 등)
- **추가 의존성**: AndroidX Test (core, rules, runner), Espresso
- **사용법**: `kuringPrimitive("android-test")`

### DSL 헬퍼

플러그인 설정을 간소화하는 확장 함수들입니다. `dsl/` 패키지에서 임포트하여 사용합니다.

#### ProjectDsl.kt
- **함수**:
  - `kuring(convention: String)` - 컨벤션 플러그인 로드 (예: `kuring("application")`)
  - `kuringPrimitive(primitive: String)` - 프리미티브 플러그인 로드 (예: `kuringPrimitive("hilt")`)
- **목적**: 모듈 `build.gradle.kts` 파일에서 플러그인을 적용하는 주요 API 제공
- **예시**:
  ```kotlin
  plugins {
      id("com.ku-stacks.ku-ring.convention.application")
  }
  kuring("application")
  ```

#### AndroidGradleDsl.kt
- **함수**:
  - `androidExtension()` - 설정을 위한 Android 확장 가져오기
  - `configureAndroidLibrary()` - 표준 Android 라이브러리 설정 적용
- **목적**: 공통 Android 설정 헬퍼
- **AGP 9.0 참고**: `androidExtension()`은 `CommonExtension`을 반환하며 (타입 파라미터 없음), 블록 할당 시 반드시 `.apply {}`를 사용해야 합니다

#### GradleDsl.kt
- **함수**:
  - `libs` - 버전 카탈로그 접근
  - `implementation()`, `testImplementation()` 등 - 의존성 헬퍼
  - `library()`, `version()` - 버전 카탈로그 접근자
- **목적**: 간결한 의존성 선언 API

#### VersionCatalogDsl.kt
- **함수**:
  - 라이브러리 및 버전에 대한 버전 카탈로그 접근자
- **목적**: `libs` 버전 카탈로그에 대한 타입 안전 접근

## 주요 작업

### 새 피처 모듈 추가하기

1. `feature/` 아래에 새 모듈 디렉토리 생성
2. `build.gradle.kts` 파일 생성:
   ```kotlin
   plugins {
       id("com.ku-stacks.ku-ring.convention.compose")
   }
   kuring("compose")
   ```
3. 모듈에 Kotlin, Android, Hilt, Compose, 단위 테스트가 자동으로 포함됩니다

### 새 유틸리티/라이브러리 모듈 추가하기

1. 모듈 디렉토리 생성
2. 원하는 컨벤션에 맞게 `build.gradle.kts` 파일 생성:
   - Compose 라이브러리: `kuring("compose")`
   - 뷰 기반 라이브러리: `kuring("view-based-feature")`
   - Kotlin 전용: `kuring("kotlin-jvm")`

### 새 프리미티브 플러그인 추가하기

1. `src/main/kotlin/com/ku_stacks/ku_ring/buildlogic/primitive/NewPlugin.kt` 파일 생성
2. 설정 내용을 담아 `Plugin<Project>` 구현
3. 버전 카탈로그에 등록하거나 `kuringPrimitive()`로 사용 가능하게 설정
4. `./gradlew :build-logic:build`로 테스트

### 플러그인 동작 커스터마이징

플러그인은 DSL 헬퍼를 통해 버전 카탈로그(`libs`)와 Android 확장에 접근합니다. 다음 방법으로 재정의할 수 있습니다:

1. 모듈에서 직접 확장에 접근: `android { ... }`
2. 프리미티브를 조합하고 확장하는 커스텀 컨벤션 플러그인 생성
3. 특정 모듈에서 세밀한 제어를 위해 `kuringPrimitive()` 사용

## 빌드 시스템 참고 사항

### AGP 9.0 마이그레이션

이 모듈은 **AGP 9.0** (Android Gradle Plugin)을 대상으로 합니다. 주요 호환성 참고 사항:

- `CommonExtension`은 타입 파라미터가 없으며 블록 스타일 람다를 지원하지 않습니다
- `defaultConfig { ... }` 대신 `androidExtension().defaultConfig.apply { ... }` 사용
- 헬퍼: `fun Project.androidExtension(): CommonExtension = extensions.getByType(CommonExtension::class)`
- `android.builtInKotlin=true`(AGP 9.0 기본값)인 경우 `kotlin-android` 플러그인을 제거해야 합니다

### 플러그인 테스트

모듈을 로컬에서 빌드하여 테스트합니다:
```bash
./gradlew :build-logic:build
```

전체 프로젝트를 다시 빌드하지 않고도 모든 플러그인의 유효성을 검사할 수 있습니다.


### 의존성 이슈

버전 카탈로그의 알려진 기존 이슈:
- 일부 모듈에서 `play-services-location:21.0.2` 버전 누락
- 일부 모듈에서 `okhttp3:logging-interceptor` 버전 누락

## AI 에이전트 안내

**주요 책임 사항**:
- 컨벤션 플러그인은 프리미티브를 조합합니다. 기능을 추가할 때는 올바른 계층에 추가하세요.
- 프리미티브 변경은 해당 프리미티브를 사용하는 **모든** 모듈에 영향을 미칩니다. `./gradlew :build-logic:build`로 충분히 테스트하세요.
- 일관성을 위해 DSL 헬퍼(`kuring()`, `kuringPrimitive()`, `androidExtension()`)를 사용하세요.
- 기존 플러그인 조합을 유지하고, 가능하면 기존 플러그인을 수정하는 대신 새 프리미티브를 추가하세요.

**자주 묻는 질문**:
- "FeaturePlugin이 적용하는 플러그인은?" - FeaturePlugin 섹션 참고
- "Hilt를 사용하려면?" - HiltPlugin 적용 또는 `kuring("feature")` 컨벤션 사용
- "Compose 지원을 추가하려면?" - `kuring("compose")` 컨벤션 사용 또는 ComposePlugin 프리미티브 추가
- "Java 버전 타겟은?" - Java 17 (KotlinPlugin에서 설정)

**주의**: 빌드 시스템은 핵심 인프라입니다. 변경 사항은 반드시 전체 빌드 테스트로 검증하세요.
