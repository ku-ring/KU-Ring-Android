---
name: add-domain-module
description: "KU-Ring Android 프로젝트의 domain/ 패키지에 새 모듈을 추가합니다. build.gradle.kts, 리포지토리 인터페이스, 유즈케이스, settings.gradle.kts 등록까지 전체 scaffolding을 처리합니다. 사용자가 domain 모듈 추가/생성, 새 도메인 패키지 생성, 유즈케이스 모듈 추가, 리포지토리 인터페이스 모듈 생성, 비즈니스 로직 모듈 분리, 새 기능의 domain 레이어 구성 등을 요청할 때 반드시 이 스킬을 사용하세요. 'domain/', 'usecase', 'repository interface'를 포함하는 모듈 생성 요청에도 트리거됩니다."
---

# Domain 모듈 추가 스킬

KU-Ring Android 프로젝트에서 `domain/` 패키지 아래에 새로운 모듈을 추가하는 스킬입니다.

## 프로젝트 컨텍스트

이 프로젝트는 Clean Architecture 기반의 멀티 모듈 Android 프로젝트입니다. 도메인 레이어는 비즈니스 로직을 담당하며, Android 프레임워크에 의존하지 않는 순수 Kotlin 모듈입니다.

**의존성 흐름:** Feature → Domain → Data

도메인 모듈은 리포지토리 **인터페이스**와 유즈케이스만 포함합니다. 구현체는 `data/` 레이어에 위치합니다.

## 모듈 추가 절차

사용자가 모듈 이름을 제공하면 (예: `"notice"`, `"library"`), 아래 단계를 순서대로 수행합니다.

### 1단계: 디렉토리 및 build.gradle.kts 생성

`domain/{moduleName}/` 디렉토리를 만들고 `build.gradle.kts`를 생성합니다.

```kotlin
plugins {
    kuring("kotlin")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.data.domain)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
```

**의존성 추가 판단 기준:**
- 페이징이 필요한 경우: `implementation(libs.androidx.paging.common.ktx)` 추가
- 날짜/시간 처리가 필요한 경우: `implementation(libs.kotlinx.datetime)` 추가
- 위 외의 의존성은 사용자에게 확인 후 추가

### 2단계: 소스 디렉토리 생성

```
domain/{moduleName}/src/main/java/com/ku_stacks/ku_ring/domain/{moduleName}/
├── repository/
└── usecase/
```

### 3단계: 리포지토리 인터페이스 생성

`repository/{Name}Repository.kt` 파일을 생성합니다.

```kotlin
package com.ku_stacks.ku_ring.domain.{moduleName}.repository

interface {Name}Repository {
    // 사용자가 요청한 메서드 정의
}
```

리포지토리 인터페이스 작성 원칙:
- **suspend** 함수를 기본으로 사용 (일회성 요청)
- 스트림이 필요한 경우 `Flow<T>` 반환
- 실패 가능한 작업은 `Result<T>` 반환 타입 사용
- `data:domain` 모듈의 도메인 모델만 참조 (Android 클래스 금지)
- KDoc으로 각 메서드의 역할을 한국어로 문서화
- **도메인 모델(data class)을 리포지토리 파일 안에 정의하지 않는다.** 도메인 모델은 `data:domain` 모듈(`data/domain/src/main/java/com/ku_stacks/ku_ring/domain/`)에 위치해야 한다. 새 모델이 필요하면 `data:domain`에 추가하고 import해서 사용한다.
  - 기존 도메인 모델 예시: `User.kt`, `Club.kt`, `Notice.kt`, `Staff.kt`, `Department.kt`
  - 새 모델이 필요한 경우 사용자에게 `data:domain`에 모델을 추가할지 확인한다

### 4단계: 유즈케이스 생성 (필요한 경우)

`usecase/{Name}UseCase.kt` 파일을 생성합니다.

```kotlin
package com.ku_stacks.ku_ring.domain.{moduleName}.usecase

import com.ku_stacks.ku_ring.domain.{moduleName}.repository.{Name}Repository
import javax.inject.Inject

class {ActionName}UseCase @Inject constructor(
    private val {name}Repository: {Name}Repository
) {
    suspend operator fun invoke(/* params */): Result<T> = runCatching {
        {name}Repository.someMethod(/* params */)
    }
}
```

유즈케이스 작성 원칙:
- `@Inject constructor`로 리포지토리를 주입받음
- `operator fun invoke()`로 호출 가능하게 만듦
- 단일 책임: 하나의 유즈케이스는 하나의 비즈니스 로직만 수행
- 유즈케이스에서 다른 유즈케이스를 호출하지 않음
- 리포지토리 메서드가 `Result<T>`를 반환하지 않는 경우 `runCatching`으로 감싸서 `Result<T>`로 반환한다 (기존 `RegisterUserUseCase` 패턴)
- 리포지토리 메서드가 이미 `Result<T>`를 반환하면 그대로 위임한다 (중복 래핑 금지)

### 5단계: settings.gradle.kts에 모듈 등록

`settings.gradle.kts`의 `// domain modules` 섹션에 새 모듈을 추가합니다.

```kotlin
// domain modules
":domain:club",
":domain:user",
// ...기존 모듈들...
":domain:{moduleName}",  // 알파벳 순서로 삽입
```

### 6단계: 빌드 검증

```bash
./gradlew :domain:{moduleName}:build
```

## 사용자에게 안내할 후속 작업

모듈 생성이 완료되면 사용자에게 다음 후속 작업을 안내합니다:

1. **data 모듈 생성**: `data/{moduleName}/`에 리포지토리 구현체 작성
2. **DI 바인딩**: `app/di/`에 Hilt 모듈을 추가하여 인터페이스와 구현체 연결
3. **feature 모듈에서 사용**: `implementation(projects.domain.{moduleName})` 의존성 추가

## 주의사항

- `android.*`, `androidx.*` 임포트 금지 (순수 Kotlin 모듈)
- `data:domain` 모듈의 도메인 모델만 사용
- 패키지명은 항상 `com.ku_stacks.ku_ring.domain.{moduleName}`
- 모듈명은 소문자, 필요시 camelCase (기존 패턴: `academicevent`, `noticecomment`)
