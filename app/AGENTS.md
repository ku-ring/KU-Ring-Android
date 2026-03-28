<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# App 모듈: AI 에이전트 안내

**app** 모듈은 KU-Ring의 메인 애플리케이션 진입점입니다. 전체 의존성 그래프를 연결하고 앱 시작 시 초기화를 수행합니다. 이 모듈은 영향 범위가 크며, 여기서의 변경은 전체 애플리케이션에 영향을 미칩니다.

## 모듈 개요

### 디렉토리 구조

```
app/
├── src/
│   ├── main/java/com/ku_stacks/ku_ring/
│   │   ├── KuRingApplication.kt        # @HiltAndroidApp 진입점
│   │   ├── di/                          # Hilt 의존성 주입 모듈
│   │   ├── initializer/                 # App Startup 초기화 클래스
│   │   └── navigator/                   # 내비게이션 구현
│   ├── androidTest/                     # 계측 테스트 (기기/에뮬레이터)
│   └── test/                            # 단위 테스트
├── build.gradle.kts                     # 모듈 빌드 설정
├── proguard-rules.pro                   # ProGuard/R8 난독화 규칙
├── signing/                             # 릴리즈 서명 설정
│   ├── keystore.properties              # 서명 자격 증명
│   └── ku_ring_keystore.jks             # 키스토어 파일
├── schemas/                             # Room 데이터베이스 스키마 스냅샷
└── release/                             # 릴리즈 빌드 산출물
```

### 빌드 설정

**사용된 컨벤션 플러그인** (`build-logic/` 기준):
- `kuring("application")` - 애플리케이션 플러그인 (Compose, Hilt 등)
- `kuringPrimitive("test")` - JUnit5 + Robolectric 단위 테스트
- `kuringPrimitive("android-test")` - Espresso + 계측 테스트
- `com.google.gms.google-services` - Firebase 서비스
- `com.google.firebase.crashlytics` - 크래시 리포팅
- `com.google.android.gms.oss-licenses-plugin` - 오픈소스 라이선스

### 의존성

**Core 모듈:**
```
core:util, core:firebase-analytics, core:firebase-crashlytics,
core:firebase-messaging, core:compose-locals, core:work
```

**데이터 레이어:**
```
data:domain, data:club, data:noticecomment, data:report,
data:academicevent, data:notification, data:place
```

**도메인 레이어:**
```
domain:navigation
```

**Feature 모듈:**
```
feature:auth, feature:club, feature:edit_subscription, feature:feedback,
feature:library, feature:notice_detail, feature:notion, feature:onboarding,
feature:splash, feature:main, feature:kuringbot, feature:edit_departments,
feature:notification
```

**서드파티:**
- Firebase BOM (analytics, crashlytics)
- Google Play Services (Auth, OSS Licenses)
- AndroidX Startup (앱 초기화)
- WorkManager (백그라운드 작업)
- LeakCanary (debug 빌드 전용)
- Espresso + Navigation Testing (계측 테스트)

---

## 주요 컴포넌트

### KuRingApplication.kt

**역할**: 전체 앱의 진입점.

**책임:**
- Hilt 의존성 주입 활성화를 위해 `@HiltAndroidApp` 적용
- Firebase, analytics, 크래시 리포팅 초기화
- `androidx.startup` 초기화 클래스 트리거
- 전역 앱 레벨 설정 구성

**중요**: 이 파일의 변경은 모든 feature에 영향을 미칩니다. `./gradlew :app:build`로 테스트하세요.

### DI 모듈 (`di/`)

**역할**: 앱에 의존성을 제공하는 Hilt 모듈 정의.

**일반적인 구조:**
- `NavigatorModule.kt` - 내비게이션 라우터 제공
- 데이터 소스 바인딩 - 레포지토리 및 원격/로컬 데이터 소스 제공
- Firebase 설정 - Firebase 인스턴스 제공

**중요 사항**: DI 모듈은 반드시:
1. `@HiltAndroidApp`에 의해 감지 가능해야 함 (동일 패키지 또는 자동 감지)
2. feature 모듈 간 순환 의존성이 없어야 함
3. feature 모듈이 요구하는 모든 의존성을 제공해야 함

**DI 변경 테스트:**
```bash
./gradlew :app:build
```

### 초기화 클래스 (`initializer/`)

**역할**: 앱 시작 시 설정 코드 실행.

**예시: TimberInitializer.kt**
- Timber (로깅 라이브러리) 설정
- 앱 실행 시 (MainActivity 이전) 동작

**패턴:**
```kotlin
@HiltAndroidApp
class KuRingApplication : Application()
```

그 다음 `Initializer<Unit>`을 구현하는 초기화 클래스 생성:
```kotlin
class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(...)
    }
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
```

### 내비게이터 (`navigator/`)

**역할**: `domain:navigation`의 내비게이션 계약 구현.

**핵심 파일: KuringNavigatorImpl.kt**
- 모든 내비게이션 라우팅 처리 (탭 전환, feature 실행 등)
- 내비게이션이 필요한 Feature 모듈에 주입
- 내부적으로 Compose Navigation 사용

**내비게이션 변경 테스트:**
```bash
./gradlew :app:connectedAndroidTest
```

---

## 빌드 변형

### Debug 빌드

- **앱 이름**: "App Name Debug"
- **런처 아이콘**: 개발용 아이콘 (시각적 구분 표시)
- **Application ID**: `com.ku_stacks.ku_ring.debug` (접미사 `.debug`)
- **Min SDK**: 24
- **Target SDK**: 35
- **Compile SDK**: 36

**빌드:**
```bash
./gradlew assembleDebug
```

### Release 빌드

- **앱 이름**: "KU-Ring" (프로덕션)
- **런처 아이콘**: 프로덕션 아이콘
- **Application ID**: `com.ku_stacks.ku_ring`
- **서명**: `app/signing/keystore.properties` 필요
- **난독화**: `proguard-rules.pro`의 R8 난독화 규칙 적용

**빌드 (서명 설정 필요):**
```bash
./gradlew assembleRelease
```

**서명 설정 위치:**
```
app/signing/keystore.properties
app/signing/ku_ring_keystore.jks
```

---

## 테스팅

### 단위 테스트

**범위**: 앱 레벨 DI 설정, 초기화 클래스, 내비게이션 로직.

**프레임워크**: JUnit5 + Robolectric (Android 프레임워크 모킹).

**실행:**
```bash
./gradlew :app:testDebugUnitTest
```

**위치:** `app/src/test/`

### 계측 테스트

**범위**: 실제 Android 프레임워크를 사용한 통합 테스트 (기기/에뮬레이터).

**예시: SmokeTest.kt**
- 앱 시작 및 기본 플로우 테스트
- Android 기기 또는 에뮬레이터 필요

**프레임워크**: Espresso + Navigation Testing.

**실행:**
```bash
./gradlew :app:connectedAndroidTest
```

**위치:** `app/src/androidTest/`

---

## Room 데이터베이스 스키마

app 모듈에서 Room 데이터베이스 스키마를 관리합니다:

- **스키마 스냅샷**: `app/schemas/` - 마이그레이션을 위한 스키마 버전을 추적하는 JSON 파일
- `build.gradle.kts`에서 **위치 설정**:
  ```kotlin
  javaCompileOptions {
      annotationProcessorOptions {
          arguments(mapOf("room.schemaLocation" to "$projectDir/schemas"))
      }
  }
  ```
- **AndroidTest 에셋**: 마이그레이션 테스트를 위해 androidTest 에셋에 스키마 포함

**스키마 스냅샷 추가 시기:**
- `data:local`에 새 Room 엔티티 추가 후
- `./gradlew :app:build` 실행하여 자동 생성
- 스키마 이력 추적을 위해 스냅샷 커밋

---

## 일반 작업

### 새 Feature 연결

1. **feature 모듈 생성** (예: `feature:newfeature`)
   - 컨벤션 플러그인 사용: `kuring("feature")`
   - 화면, ViewModel 구현

2. **app 의존성 업데이트**
   - `app/build.gradle.kts`에 추가:
     ```kotlin
     implementation(projects.feature.newfeature)
     ```

3. **내비게이션 업데이트**
   - `domain:navigation`에 라우트 추가
   - `navigator/KuringNavigatorImpl.kt`에 구현

4. **필요 시 DI 추가**
   - `app/di/`에 Hilt `@Module` 생성
   - feature 전용 의존성 제공

5. **테스트**
   ```bash
   ./gradlew :app:build
   ./gradlew :app:testDebugUnitTest
   ```

### 전역 초기화 클래스 추가

1. `app/initializer/`에 클래스 생성
2. `androidx.startup`의 `Initializer<Unit>` 구현
3. `AndroidManifest.xml`에 등록 (매니페스트를 통해 자동 감지)
4. 테스트: `./gradlew :app:testDebugUnitTest`

### DI 바인딩 업데이트

1. `app/di/`의 모듈 수정
2. 모듈 컴파일 테스트: `./gradlew :app:build`
3. 주입 실패 확인을 위한 전체 테스트 실행:
   ```bash
   ./gradlew test
   ```

---

## 문제 해결

### 빌드 실패

**증상**: Gradle 동기화 또는 빌드 실패

1. **Gradle 캐시 정리**:
   ```bash
   rm -rf .gradle build
   ./gradlew clean
   ```

2. **IDE 캐시 무효화** (Android Studio: File → Invalidate Caches)

3. **컨벤션 플러그인 확인**: `build-logic/`가 정상적으로 빌드되는지 확인
   ```bash
   ./gradlew :build-logic:build
   ```

### Hilt 오류

**증상**: `Unable to find Hilt binding for ...`

1. `app/di/`의 DI 모듈이 해당 타입을 제공하는지 확인
2. 모듈이 `@HiltAndroidApp`에 의해 감지되는지 확인
3. feature 모듈 간 순환 의존성 확인
4. 테스트:
   ```bash
   ./gradlew :app:build
   ```

**증상**: `@HiltViewModel not found`

1. `kuring("application")` 컨벤션 플러그인이 적용되었는지 확인
2. Hilt 플러그인 버전이 `libs.versions.toml`과 일치하는지 확인

### 내비게이션 문제

**증상**: Feature가 내비게이션에 표시되지 않음

1. feature 모듈이 `app/build.gradle.kts`에 추가되었는지 확인
2. 라우트가 `domain:navigation`에 정의되었는지 확인
3. `navigator/KuringNavigatorImpl.kt`의 구현 확인
4. 계측 테스트로 확인:
   ```bash
   ./gradlew :app:connectedAndroidTest
   ```

### Compose 컴파일

**증상**: `Compose version mismatch` 또는 컴파일러 오류

1. `gradle/libs.versions.toml`의 버전 확인:
   - Compose BOM
   - Compose Compiler
   - Kotlin 버전 (Compose 컴파일러와 일치해야 함)
2. 실행: `./gradlew clean build`

### Firebase 문제

**증상**: Firebase 초기화 실패

1. `google-services.json`이 `app/src/main/`에 있는지 확인
2. Firebase gradle 플러그인이 `build.gradle.kts`에 적용되었는지 확인
3. DI 모듈이 Firebase 인스턴스를 제공하는지 확인
4. 테스트:
   ```bash
   ./gradlew :app:build
   ```

---

## 이해해야 할 주요 파일

app 모듈을 변경하기 전에 다음 파일들을 읽어보세요:

| 파일 | 이유 |
|------|-----|
| `KuRingApplication.kt` | 앱 진입점, Hilt 설정 |
| `app/di/NavigatorModule.kt` | 내비게이션 의존성 제공 |
| `app/navigator/KuringNavigatorImpl.kt` | 내비게이션 라우트 동작 방식 |
| `app/build.gradle.kts` | 모듈 의존성 및 변형 |
| `proguard-rules.pro` | 코드 난독화 규칙 (릴리즈 빌드) |
| `gradle/libs.versions.toml` | 의존성 버전 (상위) |

---

## 에이전트 가이드라인

### DI 수정 시

- **항상 테스트**: `./gradlew :app:build`
- **순환 의존성 없는지 확인**: feature 모듈 임포트 점검
- **새 모듈 문서화**: 상위 `AGENTS.md`의 모듈 목록에 추가

### Feature 추가 시

- **순서가 중요합니다**: feature 구현 → app 의존성 추가 → 내비게이션 연결 → DI 추가
- **단계적으로 테스트**: 각 단계 후 빌드
- **임포트를 가정하지 마세요**: Hilt는 명시적 모듈 선언이 필요합니다

### 디버깅 시

- **app 모듈부터 시작**: 대부분의 실패는 DI 또는 내비게이션으로 추적됨
- **gradle 출력 확인**: AGP 9.0+는 상세한 오류 메시지를 제공
- **독립적으로 테스트**: 전체 테스트 스위트 전에 `./gradlew :app:testDebugUnitTest` 사용

### 브레이킹 체인지 시

- **전체 테스트 스위트 실행**: `./gradlew test`
- **계측 테스트 실행**: `./gradlew :app:connectedAndroidTest`
- **Room 스키마 확인**: `./gradlew :app:build` 실행하여 스키마 문제 없는지 확인

---

## AGP 9.0 마이그레이션 노트

이 프로젝트는 **AGP 9.0** (Android Gradle Plugin)을 사용합니다. 주요 차이점:

- `CommonExtension`에 **타입 파라미터 없음**
- 블록 람다 대신 **프로퍼티 접근 + `.apply {}`** 사용
- `android.builtInKotlin=true`일 때 `kotlin-android` 플러그인을 **반드시 제거**해야 함
- 자세한 마이그레이션 정보는 상위 `AGENTS.md` 참고

**예시 (AGP 9.0 올바른 패턴):**
```kotlin
fun Project.configureAndroid() {
    androidExtension().defaultConfig.apply {
        minSdk = 24
        targetSdk = 35
    }
}
```

---

## 연락처

- **모듈 담당**: KU-Ring 개발팀
- **문의**: kuring.korea@gmail.com
- **코드 리뷰**: PR 템플릿 사용 (GitHub)
