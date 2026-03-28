<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# Gradle 설정: AI 에이전트 안내

KU-Ring Android 프로젝트의 Gradle 설정 및 의존성 관리 디렉토리입니다. Gradle 래퍼, 중앙화된 버전 카탈로그, 빌드 유틸리티가 포함되어 있습니다.

---

## 디렉토리 구조

```
gradle/
├── wrapper/
│   ├── gradle-wrapper.jar          # Gradle 래퍼 실행 JAR
│   └── gradle-wrapper.properties   # Gradle 래퍼 버전 및 다운로드 URL
├── libs.versions.toml              # 중앙화된 버전 카탈로그 (단일 진실 공급원)
├── gradle-daemon-jvm.properties    # Gradle 데몬의 JVM 설정
└── dependencyGraph.gradle          # 모듈 의존성 그래프 생성 유틸리티 스크립트
```

---

## 주요 파일

| 파일 | 설명 |
|------|---------|
| `libs.versions.toml` | **모든 의존성 버전의 단일 진실 공급원.** 모든 모듈에서 사용하는 버전, 라이브러리, 번들, 플러그인을 정의합니다. |
| `wrapper/gradle-wrapper.properties` | Gradle 버전 명세 (현재 AGP 9.1.0, Kotlin 2.3.20). |
| `dependencyGraph.gradle` | 모듈 의존성 그래프를 PNG로 시각화하는 Gradle 태스크 스크립트. Graphviz가 필요합니다. |
| `gradle-daemon-jvm.properties` | Gradle 데몬 성능 향상을 위한 JVM 메모리 및 옵션 튜닝. |

---

## 버전 카탈로그 (libs.versions.toml)

버전 카탈로그는 **의존성 버전을 정의하거나 업데이트하는 유일한 장소**입니다. 모든 모듈은 하드코딩된 문자열 대신 이 파일의 버전 카탈로그를 참조해야 합니다.

### 주요 버전

```toml
[versions]
android-gradle = "9.1.0"              # Android Gradle Plugin
kotlin = "2.3.20"                     # Kotlin 언어
compose-bom = "2026.03.01"            # Jetpack Compose Bill of Materials
minSdk = "24"                         # 최소 Android SDK
targetSdk = "35"                      # 타겟 Android SDK
compileSdk = "36"                     # 컴파일 Android SDK
appVersion = "2.4.3"                  # 애플리케이션 시맨틱 버전
versionCode = "20404"                 # Android 빌드 버전 코드
```

### 주요 라이브러리

**Kotlin & Coroutines**
- `kotlinx-coroutines` = "1.10.2"
- `kotlinx-serialization-json` = "1.10.0"
- `kotlinx-datetime` = "0.7.1"

**Android & Jetpack**
- `androidx-lifecycle` = "2.10.0"
- `androidx-room` = "2.8.4"
- `androidx-work` = "2.11.2"
- `androidx-navigation` = "2.9.7"
- `androidx-compose` (BOM) = "2026.03.01"

**네트워킹 & HTTP**
- `ktor` = "3.4.2" (HTTP 클라이언트)
- `retrofit` = "3.0.0" (REST 클라이언트)
- `okhttp` = "5.3.2" (HTTP 엔진)

**Firebase & 서비스**
- `firebase` = "34.11.0" (Firebase BOM)
- `google-services` = "4.4.4" (Google Services 플러그인)
- `hilt` = "2.59.2" (의존성 주입)

**테스트**
- `junit5` = "6.0.3"
- `androidx-espresso` = "3.7.0"

---

## 하위 디렉토리

| 디렉토리 | 설명 |
|-----------|---------|
| `wrapper/` | 머신 간 재현 가능한 빌드를 위한 Gradle 래퍼 파일. 버전은 `gradle-wrapper.properties`에 명시됩니다. |

---

## AI 에이전트 안내

### 의존성 업데이트

의존성을 추가하거나 업데이트할 때:

1. **버전 카탈로그를 먼저 수정**: `gradle/libs.versions.toml` 편집
   - `[versions]` 섹션에서 버전을 추가하거나 변경
   - `[libraries]` 섹션에 라이브러리 항목 추가
   - 관련 라이브러리가 있으면 선택적으로 `[bundles]`에 추가

2. **모듈 build.gradle.kts에서 참조**:
   ```kotlin
   // 이렇게 하지 마세요:
   implementation("com.example:library:1.2.3")

   // 이렇게 하세요:
   implementation(libs.com.example.library)
   ```

3. **변경 사항 테스트**:
   ```bash
   ./gradlew clean build
   ./gradlew :app:assembleDebug
   ```

### 의존성 그래프 생성

모듈 의존성을 PNG 그래프로 시각화하려면:

```bash
# macOS (Graphviz 필요)
brew install graphviz
./gradlew projectDependencyGraph

# Windows (https://graphviz.org/download/ 에서 Graphviz 설치)
gradlew projectDependencyGraph

# 출력 파일: 저장소 루트의 project.dot.png
```

### 자주 사용하는 태스크

```bash
# 설정된 모든 버전 나열
./gradlew dependencies --dry-run

# 의존성 업데이트 확인
./gradlew dependencyUpdates

# 버전 카탈로그 변경 후 Gradle 동기화
./gradlew --refresh-dependencies

# 동기화 실패 시 Gradle 캐시 삭제
rm -rf .gradle/
./gradlew clean
```

### AGP 9.0+ 주의 사항

이 프로젝트는 이전 버전과 호환성이 깨지는 변경 사항이 있는 **AGP 9.1.0**을 사용합니다:

- `android.builtInKotlin=true` (AGP 기본값)인 모듈에서 `kotlin-android` 플러그인을 제거해야 합니다.
- `build-logic/`의 컨벤션 플러그인이 AGP 9.0 호환성을 처리합니다.
- `android {}` 블록 스타일 문법을 사용하지 마세요 — 대신 프로퍼티 접근자와 함께 `.apply {}`를 사용하세요.

DSL 헬퍼는 `build-logic/src/main/kotlin/AndroidGradleDsl.kt`를 참고하세요.

### 문제 해결

| 문제 | 해결 방법 |
|-------|----------|
| 업데이트 후 의존성을 찾을 수 없음 | `[libraries]` 섹션에 라이브러리 항목이 있는지 확인 (버전만 있으면 안 됨). `./gradlew clean` 실행. |
| 버전 불일치 오류 | 라이브러리의 버전 참조가 `[versions]` 키와 일치하는지 확인 (예: `version.ref = "compose-bom"`). |
| Gradle 데몬 문제 | `.gradle/` 디렉토리를 삭제하고 `./gradlew --stop` 후 `./gradlew clean` 실행. |
| 의존성 그래프 실패 | Graphviz 설치: `brew install graphviz` (macOS) 또는 graphviz.org에서 다운로드. |

---

## 테스트 요구사항

**Gradle 설정을 수정하는 에이전트의 경우:**

1. `gradle/libs.versions.toml` 수정
2. 실행: `./gradlew clean build` (컴파일 성공 확인)
3. 실행: `./gradlew :app:assembleDebug` (앱 어셈블리 확인)
4. 실행: `./gradlew testDebugUnitTest` (테스트 통과 확인)
5. 순환 의존성 없음을 검증: `./gradlew projectDependencyGraph` (새 모듈 추가 시)

**새 의존성을 사용하는 에이전트의 경우:**

1. `gradle/libs.versions.toml`에 버전이 존재하는지 확인
2. 버전 카탈로그 참조를 사용하여 해당 모듈의 `build.gradle.kts`에 라이브러리 추가
3. 전체 프로젝트 동기화 실행: `./gradlew clean`
4. 영향받는 모듈 빌드: `./gradlew :module:name:build`

---

## 관련 문서

- 상위 가이드: `../AGENTS.md` — 전체 프로젝트 아키텍처 및 모듈 개요
- 빌드 로직: `../build-logic/AGENTS.md` — 컨벤션 플러그인 구현
- 의존성 문제: 루트 `AGENTS.md`의 "알려진 문제" 참고
