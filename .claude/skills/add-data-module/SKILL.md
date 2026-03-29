---
name: add-data-module
description: "KU-Ring Android 프로젝트의 data/ 패키지에 새 모듈을 추가합니다. build.gradle.kts 생성, 리포지토리 구현체, Hilt DI 바인딩, settings.gradle.kts 등록까지 전체 과정을 처리합니다. 사용자가 '데이터 모듈 추가', 'data 모듈 만들어', '새 data 패키지', '리포지토리 구현체 추가', 'data layer 모듈' 등을 언급할 때 이 스킬을 사용하세요. domain 모듈이 아닌 data 모듈에 대한 요청일 때 사용합니다."
---

# Data 모듈 추가 스킬

KU-Ring Android 프로젝트에서 `data/` 패키지 아래에 새로운 모듈을 추가하는 스킬입니다.

## 프로젝트 컨텍스트

이 프로젝트는 Clean Architecture 기반의 멀티 모듈 Android 프로젝트입니다. Data 레이어는 리포지토리 **구현체**와 데이터 소스를 담당합니다. Domain 레이어의 인터페이스를 구현하고, Remote/Local 모듈을 통해 실제 데이터를 가져옵니다.

**의존성 흐름:** Feature -> Domain <- Data

Data 모듈은 `kuring("feature")` 컨벤션 플러그인을 사용하는 Android 라이브러리 모듈이며, Hilt DI가 자동으로 설정됩니다.

## 모듈 추가 전 확인사항

모듈 생성을 시작하기 전에 사용자에게 확인할 것:

1. **모듈 이름** (예: `"notice"`, `"library"`)
2. **데이터 소스 유형** - 어떤 조합이 필요한지:
   - Remote만 (API 호출만) - 가장 일반적
   - Local만 (Room DB만)
   - Remote + Local (캐싱, 오프라인 지원)
3. **대응하는 domain 모듈 존재 여부** - `domain/{moduleName}/`이 이미 있는지 확인
4. **API 통신 라이브러리** - Retrofit 또는 Ktor (Retrofit이 기본)

## 모듈 추가 절차

### 1단계: build.gradle.kts 생성

`data/{moduleName}/build.gradle.kts`를 생성합니다. 데이터 소스 유형에 따라 플러그인과 의존성을 조합합니다.

**기본 템플릿 (Remote만 사용하는 가장 일반적인 형태):**

```kotlin
import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
}

android {
    setNameSpace("{moduleName}")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.data.remote)
    implementation(projects.domain.{moduleName})
}
```

**플러그인 추가 판단 기준:**
- Remote API 사용 (`data:remote` 의존): `kuringPrimitive("retrofit")` 추가 — API를 호출하는 모듈이면 반드시 포함
- Ktor 사용: `kuringPrimitive("ktor")` 추가
- OkHttp 직접 사용: `kuringPrimitive("okhttp")` 추가
- Room DB 사용: `kuringPrimitive("room")` 추가
- 테스트 작성: `kuringPrimitive("test")` 추가 + testOptions 블록

`data:remote`에 의존하면서 `kuringPrimitive("retrofit")`을 빠뜨리면 컴파일 에러가 발생하므로 주의.

**의존성 추가 판단 기준:**
- `projects.core.util` - 거의 항상 필요 (`suspendRunCatching` 등 유틸리티)
- `projects.data.domain` - 도메인 모델 참조 시
- `projects.data.remote` - API 클라이언트 사용 시
- `projects.data.local` - Room DB 사용 시
- `projects.domain.{moduleName}` - 대응하는 도메인 모듈의 리포지토리 인터페이스 구현 시
- `libs.kotlinx.datetime` - 날짜/시간 변환이 필요한 경우
- `libs.bundles.paging` - 페이징 처리가 필요한 경우

**테스트 포함 시 추가되는 내용:**

```kotlin
plugins {
    // ...
    kuringPrimitive("test")
}

android {
    setNameSpace("{moduleName}")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // ...
    testImplementation(libs.kotlinx.coroutines.test)
}
```

### 2단계: 소스 디렉토리 생성

데이터 소스 유형에 따라 적절한 디렉토리를 생성합니다.

**Remote만 사용하는 간단한 구조:**
```
data/{moduleName}/src/main/java/com/ku_stacks/ku_ring/{moduleName}/
├── di/
│   └── RepositoryModule.kt
└── repository/
    └── {Name}RepositoryImpl.kt
```

**Remote + Local + Mapper가 필요한 풍부한 구조:**
```
data/{moduleName}/src/main/java/com/ku_stacks/ku_ring/{moduleName}/
├── di/
│   └── RepositoryModule.kt
├── mapper/
│   ├── ResponseToDomainMapper.kt
│   └── EntityToDomainMapper.kt
├── source/
│   ├── {Name}RemoteDataSource.kt
│   └── {Name}LocalDataSource.kt
└── repository/
    └── {Name}RepositoryImpl.kt
```

모듈이 매우 간단한 경우 (리포지토리 구현체가 하나뿐인 경우), `repository/` 하위 디렉토리 없이 패키지 루트에 `{Name}RepositoryImpl.kt`를 직접 놓을 수 있습니다. 기존 모듈의 패턴을 따릅니다 (예: `data/report`는 루트에 직접 배치).

### 3단계: Hilt DI 모듈 생성

`di/RepositoryModule.kt`를 생성합니다. 이 파일은 리포지토리 인터페이스와 구현체를 바인딩합니다.

**도메인 모듈의 인터페이스를 구현하는 경우:**

```kotlin
package com.ku_stacks.ku_ring.{moduleName}.di

import com.ku_stacks.ku_ring.domain.{moduleName}.repository.{Name}Repository
import com.ku_stacks.ku_ring.{moduleName}.repository.{Name}RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bind{Name}Repository(
        repositoryImpl: {Name}RepositoryImpl,
    ): {Name}Repository
}
```

**data 모듈 내에 인터페이스도 함께 있는 경우 (domain 모듈이 없을 때):**

```kotlin
package com.ku_stacks.ku_ring.{moduleName}.di

import com.ku_stacks.ku_ring.{moduleName}.repository.{Name}Repository
import com.ku_stacks.ku_ring.{moduleName}.repository.{Name}RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bind{Name}Repository(
        repositoryImpl: {Name}RepositoryImpl,
    ): {Name}Repository
}
```

`@Singleton` 어노테이션은 리포지토리가 앱 전체에서 하나의 인스턴스만 필요한 경우에 추가합니다. 기존 모듈을 보면 붙이는 경우와 안 붙이는 경우가 모두 있으므로, 상황에 맞게 판단합니다.

### 4단계: 리포지토리 구현체 생성

`repository/{Name}RepositoryImpl.kt`를 생성합니다.

```kotlin
package com.ku_stacks.ku_ring.{moduleName}.repository

import com.ku_stacks.ku_ring.domain.{moduleName}.repository.{Name}Repository
import com.ku_stacks.ku_ring.remote.{moduleName}.{Name}Client
import com.ku_stacks.ku_ring.util.suspendRunCatching
import javax.inject.Inject

class {Name}RepositoryImpl @Inject constructor(
    private val {name}Client: {Name}Client,
) : {Name}Repository {
    override suspend fun someMethod(): Result<Unit> = suspendRunCatching {
        {name}Client.someApiCall()
    }
}
```

리포지토리 구현체 작성 원칙:
- `@Inject constructor`로 데이터 소스/클라이언트를 주입받음
- `suspendRunCatching`을 사용하여 예외를 `Result`로 감싸기 (`com.ku_stacks.ku_ring.util`에 위치)
- Remote 클라이언트는 `data:remote` 모듈에 이미 정의되어 있다고 가정 (없으면 생성 필요성을 사용자에게 안내)
- 매퍼가 필요한 경우 `mapper/` 패키지에 확장 함수나 클래스로 작성

### 5단계: settings.gradle.kts에 모듈 등록

`settings.gradle.kts`의 `// data modules` 섹션에 새 모듈을 추가합니다.

```kotlin
// data modules
":data:domain",
// ...기존 모듈들...
":data:{moduleName}",  // 알파벳 순서로 삽입
```

### 6단계: 빌드 검증

```bash
./gradlew :data:{moduleName}:build
```

빌드 실패 시 가장 흔한 원인:
- `data:remote`에 해당 feature의 API 클라이언트가 없음 -> 사용자에게 안내
- 대응하는 `domain` 모듈이 없음 -> domain 모듈 먼저 생성 필요
- 의존성 누락 -> build.gradle.kts에 추가

## 사용자에게 안내할 후속 작업

모듈 생성이 완료되면 다음 후속 작업을 안내합니다:

1. **Remote 클라이언트 확인**: `data/remote/src/main/java/com/ku_stacks/ku_ring/remote/{moduleName}/`에 API 클라이언트가 없으면 생성 필요
2. **domain 모듈**: 대응하는 `domain/{moduleName}/`이 없으면 생성 안내 (add-domain-module 스킬 활용)
3. **feature 모듈에서 사용**: `implementation(projects.data.{moduleName})` 의존성 추가

## 주의사항

- 패키지명은 항상 `com.ku_stacks.ku_ring.{moduleName}`
- 모듈명은 소문자, 필요시 camelCase (기존 패턴: `academicevent`, `noticecomment`)
- `setNameSpace()`의 인자는 모듈명과 동일하게 설정
- `data:remote` 모듈의 클라이언트는 feature별로 하위 패키지에 정리되어 있음
- Hilt DI는 `kuring("feature")` 플러그인이 자동 설정하므로 별도 플러그인 추가 불필요
