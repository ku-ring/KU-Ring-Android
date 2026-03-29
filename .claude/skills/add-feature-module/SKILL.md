---
name: add-feature-module
description: "KU-Ring Android 프로젝트에 새로운 feature 모듈을 추가합니다. feature 모듈 생성, 새 화면 모듈 추가, feature 패키지에 모듈 만들기 등의 요청 시 사용합니다. 'feature 모듈 추가', '새 화면 만들어줘', 'feature 패키지에 모듈', '새 기능 모듈' 등의 표현이 나오면 반드시 이 스킬을 사용하세요."
---

# Feature 모듈 추가 스킬

KU-Ring Android 프로젝트의 `feature/` 패키지에 새로운 모듈을 추가하는 스킬입니다. 프로젝트의 기존 컨벤션과 아키텍처 패턴을 정확히 따릅니다.

## 사용자로부터 확인할 정보

모듈을 생성하기 전에 다음을 확인하세요:

1. **모듈 이름** (snake_case, 예: `my_feature`)
2. **모듈 목적** (어떤 화면/기능인지)
3. **필요한 domain/data 의존성** (기존 모듈 중 어떤 것을 사용하는지)
4. **추가 플러그인 필요 여부** (`retrofit`, `junit5`, `room` 등)

모듈 이름이 명확하면 바로 생성을 진행해도 됩니다.

## 생성 절차

### 1단계: 디렉토리 및 파일 생성

아래 파일들을 순서대로 생성합니다. `{module}`은 snake_case 모듈 이름, `{Module}`은 PascalCase 이름입니다.

#### build.gradle.kts

```kotlin
import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("feature.{module}")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.util)
    implementation(projects.core.composeUtil)

    implementation(libs.bundles.compose.interop)
}
```

- `kuring("compose")`는 내부적으로 `FeaturePlugin` + `ComposePlugin`을 적용합니다 (Android library, Kotlin, Hilt, Compose 모두 포함).
- `kuring("view")`는 일부 모듈에서 추가로 사용하지만, `kuring("compose")`만으로 충분한 경우가 많습니다. 기존 모듈들의 패턴을 참고하세요.
- 필요에 따라 `kuringPrimitive("retrofit")`, `kuringPrimitive("junit5")` 등을 추가합니다.

#### consumer-rules.pro

빈 파일로 생성합니다.

#### 소스 디렉토리 구조

```
feature/{module}/src/main/java/com/ku_stacks/ku_ring/{module}/compose/
```

이 경로 아래에 다음 파일들을 생성합니다:

#### {Module}Screen.kt

```kotlin
package com.ku_stacks.ku_ring.{module}.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun {Module}Screen(
    viewModel: {Module}ViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    {Module}Content(
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun {Module}Content(
    state: {Module}State,
    onEvent: ({Module}Event) -> Unit,
) {
    // TODO: UI 구현
}
```

#### {Module}ViewModel.kt

```kotlin
package com.ku_stacks.ku_ring.{module}.compose

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class {Module}ViewModel @Inject constructor(
    // TODO: UseCase 주입
) : ViewModel() {

    private val _state = MutableStateFlow({Module}State())
    val state: StateFlow<{Module}State> = _state.asStateFlow()

    fun onEvent(event: {Module}Event) {
        when (event) {
            else -> Unit // TODO: 이벤트 처리
        }
    }
}
```

#### {Module}State.kt

```kotlin
package com.ku_stacks.ku_ring.{module}.compose

data class {Module}State(
    val isLoading: Boolean = false,
)
```

#### {Module}Event.kt

```kotlin
package com.ku_stacks.ku_ring.{module}.compose

sealed class {Module}Event {
    // TODO: 이벤트 정의
}
```

#### {Module}Navigation.kt

```kotlin
package com.ku_stacks.ku_ring.{module}.compose

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

// TODO: domain/navigation에 Route 정의 후 연결
// fun NavGraphBuilder.{module}Navigation() {
//     composable<Route.{Module}> {
//         {Module}Screen()
//     }
// }
```

### 2단계: settings.gradle.kts에 모듈 등록

`settings.gradle.kts`의 feature modules 섹션에 새 모듈을 추가합니다:

```kotlin
// feature modules 블록 안에 추가
":feature:{module}",
```

알파벳 순서를 유지하면서 추가하세요.

### 3단계: 빌드 확인

```bash
./gradlew :feature:{module}:compileDebugKotlin
```

## 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| 모듈 디렉토리 | snake_case | `edit_departments` |
| 패키지명 | snake_case (모듈명 그대로) | `com.ku_stacks.ku_ring.edit_departments` |
| namespace | `feature.{module}` | `feature.edit_departments` |
| 클래스명 | PascalCase | `EditDepartmentsScreen` |
| settings.gradle.kts | `:feature:{module}` | `:feature:edit_departments` |

## 주의사항

- 네비게이션 Route는 `domain/navigation/` 모듈에 정의합니다. feature 모듈 내부에 Route를 정의하지 마세요.
- 색상/타이포그래피는 `core:designsystem` 토큰을 사용합니다. 하드코딩하지 마세요.
- ViewModel은 반드시 `@HiltViewModel`과 `@Inject constructor`를 사용합니다.
- 화면 Composable은 stateless로 작성하고, state와 onEvent를 파라미터로 받습니다.
- `libs.bundles.compose.interop`은 Compose와 Activity/Fragment 간 호환성 의존성 번들입니다. Compose 기반 feature에서 거의 항상 필요합니다.
