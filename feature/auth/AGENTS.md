<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 피처 Auth: AI 에이전트 안내

auth 피처 모듈은 로그인, 회원가입, 비밀번호 재설정, 로그아웃 등 사용자 인증을 처리합니다. 미인증 사용자의 진입점입니다.

## 모듈 개요

| 화면 | 패키지 | 목적 | 주요 클래스 |
|--------|---------|---------|------------|
| 로그인 | `signin/` | 이메일/비밀번호 로그인 | `SignInViewModel`, `SignInScreen`, `SignInState` |
| 회원가입 | `signup/` | 사용자 등록 플로우 | `SignUpViewModel`, `SignUpScreen`, 다단계 내부 화면 |
| 비밀번호 재설정 | `reset_password/` | 비밀번호 찾기 플로우 | `ResetPasswordViewModel`, `ResetPasswordScreen` |
| 로그아웃 | `signout/` | 로그아웃 다이얼로그/화면 | `SignOutViewModel`, `SignOutDialog` |
| 컴포넌트 | `component/` | 공유 인증 UI 요소 | 버튼, 텍스트 필드, 탑바 |
| 상태 | `state/` | 인증 UI 상태 관리 | 상태 클래스 (ViewModel 아님) |

## 모듈 구조

```text
feature/auth/
├── build.gradle.kts
├── src/main/java/com/ku_stacks/ku_ring/auth/
│   ├── compose/
│   │   ├── AuthScreen.kt                       # 루트 인증 네비게이션
│   │   ├── signin/
│   │   │   ├── SignInScreen.kt
│   │   │   ├── SignInViewModel.kt
│   │   │   ├── SignInState.kt
│   │   │   ├── SignInEvent.kt
│   │   │   ├── SignInNavigation.kt
│   │   │   └── inner_screen/
│   │   │       └── (현재 없음, 또는 ForgotPasswordDialog)
│   │   ├── signup/
│   │   │   ├── SignUpScreen.kt                 # 다단계 폼 오케스트레이터
│   │   │   ├── SignUpViewModel.kt
│   │   │   ├── SignUpState.kt
│   │   │   ├── SignUpEvent.kt
│   │   │   ├── SignUpSideEffect.kt
│   │   │   ├── SignUpNavigation.kt
│   │   │   └── inner_screen/
│   │   │       ├── EmailVerificationScreen.kt
│   │   │       ├── SetPasswordScreen.kt
│   │   │       ├── TermsAndConditionScreen.kt
│   │   │       └── SignUpCompleteScreen.kt
│   │   ├── reset_password/
│   │   │   ├── ResetPasswordScreen.kt
│   │   │   ├── ResetPasswordViewModel.kt
│   │   │   ├── ResetPasswordState.kt
│   │   │   ├── ResetPasswordEvent.kt
│   │   │   ├── ResetPasswordSideEffect.kt
│   │   │   ├── ResetPasswordNavigation.kt
│   │   │   └── inner_screen/
│   │   │       ├── ResetPasswordScreen.kt
│   │   │       └── EmailVerificationScreen.kt
│   │   ├── component/
│   │   │   ├── button/
│   │   │   │   ├── RoundedCornerButton.kt     # 표준 인증 버튼
│   │   │   │   ├── VerificationButton.kt      # 코드 인증용 버튼
│   │   │   │   └── ...
│   │   │   ├── textfield/
│   │   │   │   ├── PlainTextField.kt
│   │   │   │   ├── OutlinedSupportingTextField.kt
│   │   │   │   └── PasswordInputField.kt
│   │   │   ├── CodeTimer.kt                   # 인증 코드 카운트다운
│   │   │   ├── PasswordInputGroup.kt
│   │   │   └── topbar/
│   │   │       └── AuthTopBar.kt              # 인증 화면 헤더
│   └── state/
│       └── (인증 전용 상태 클래스)
├── src/main/res/
│   ├── values/
│   │   ├── strings.xml                        # 인증 문자열 (레이블, 오류, 힌트)
│   │   └── colors.xml                         # 인증 전용 색상 (있을 경우)
│   └── raw/
│       └── (이용약관, 개인정보처리방침 HTML/텍스트)
└── src/test/java/...
```

## 인증 플로우

### 로그인 플로우

1. 사용자가 이메일과 비밀번호 입력
2. `SignInViewModel.onEvent(SignIn)` 호출 → `UserRepository.signIn(email, password)`
3. 성공 시: 인증 토큰 저장 후 `main/` 화면으로 이동
4. 실패 시: 입력 필드 아래에 오류 메시지 표시

```kotlin
data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPasswordVisible: Boolean = false
)

sealed class SignInEvent {
    data class EmailChanged(val email: String) : SignInEvent()
    data class PasswordChanged(val password: String) : SignInEvent()
    data object TogglePasswordVisibility : SignInEvent()
    data object SignIn : SignInEvent()
}
```

### 회원가입 플로우

유효성 검사가 포함된 다단계 폼:

1. 이메일 인증 (코드 발송, 인증)
2. 비밀번호 설정 (비밀번호 + 확인)
3. 이용약관 동의
4. 완료 화면
5. 메인으로 이동

**각 단계는 별도의 inner_screen 컴포저블입니다:**

```kotlin
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.currentStep) {
        SignUpStep.EMAIL_VERIFICATION -> EmailVerificationScreen(viewModel)
        SignUpStep.SET_PASSWORD -> SetPasswordScreen(viewModel)
        SignUpStep.TERMS -> TermsAndConditionScreen(viewModel)
        SignUpStep.COMPLETE -> SignUpCompleteScreen(onNavigate = onNavigateToMain)
    }
}
```

### 비밀번호 재설정 플로우

1. 비밀번호 재설정을 위한 이메일 입력
2. 코드로 이메일 인증
3. 새 비밀번호 설정
4. 로그인으로 돌아가기

```kotlin
data class ResetPasswordState(
    val currentStep: ResetPasswordStep = ResetPasswordStep.EMAIL,
    val email: String = "",
    val verificationCode: String = "",
    val newPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class ResetPasswordStep {
    EMAIL, VERIFICATION, NEW_PASSWORD, SUCCESS
}
```

## 주요 컴포넌트

### 재사용 가능한 버튼

```kotlin
// RoundedCornerButton.kt
@Composable
fun RoundedCornerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(text)
        }
    }
}
```

### 텍스트 입력 필드

```kotlin
// OutlinedSupportingTextField.kt
@Composable
fun OutlinedSupportingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    supportingText: String? = null,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Email
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        supportingText = {
            if (errorMessage != null) {
                Text(errorMessage, color = Color.Red)
            } else if (supportingText != null) {
                Text(supportingText)
            }
        },
        isError = errorMessage != null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth()
    )
}
```

### 코드 타이머

이메일 인증 코드의 카운트다운 타이머 (예: 5분):

```kotlin
@Composable
fun CodeTimer(
    initialSeconds: Int = 300,
    onTimeExpired: () -> Unit = {}
) {
    var remainingTime by remember { mutableIntState(initialSeconds) }

    LaunchedEffect(Unit) {
        while (remainingTime > 0) {
            delay(1000)
            remainingTime--
        }
        onTimeExpired()
    }

    val minutes = remainingTime / 60
    val seconds = remainingTime % 60
    Text("Resend code in $minutes:${"%02d".format(seconds)}")
}
```

## 빌드 설정

```gradle
import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
}

android {
    setNameSpace("feature.auth")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.composeUtil)
    implementation(projects.core.designsystem)

    // 도메인 및 데이터 레이어
    implementation(projects.domain.user)
    implementation(projects.data.verification)

    // Compose (컨벤션 플러그인에서 관리)
    implementation(libs.bundles.compose.interop)
}
```

## 에이전트 공통 작업

### 새 인증 단계 추가 (회원가입)

1. 새 내부 화면 생성: `signup/inner_screen/NewStepScreen.kt`
2. 단계 enum 추가: `SignUpStep.NEW_STEP`
3. 새 단계를 처리하도록 `SignUpViewModel` 업데이트
4. 이벤트 추가: `data object NextStep : SignUpEvent()`
5. `SignUpState.currentStep` 로직 업데이트
6. `testDebugUnitTest`로 네비게이션 플로우 테스트

### 이메일 인증 수정

1. `EmailVerificationScreen` 컴포저블 업데이트
2. `SignUpViewModel.onEvent(VerifyEmail)` 로직 수정
3. `data:verification` 리포지토리의 API 호출 확인
4. 목 리포지토리로 테스트

### 테스트 실행

```bash
# auth 모듈 테스트
./gradlew :feature:auth:testDebugUnitTest

# 특정 ViewModel 테스트
./gradlew :feature:auth:testDebugUnitTest --tests "SignInViewModelTest"
```

### 빌드

```bash
# auth 모듈 빌드
./gradlew :feature:auth:build

# 컴파일 확인
./gradlew :feature:auth:compileDebugKotlin
```

## 주요 패턴

### 다단계 폼 관리

상태의 `currentStep`을 사용하여 내부 화면 간 이동:

```kotlin
val state by viewModel.state.collectAsStateWithLifecycle()

when (state.currentStep) {
    Step1 -> FirstStepScreen()
    Step2 -> SecondStepScreen()
    Step3 -> ThirdStepScreen()
}
```

### 오류 표시

입력 필드 아래에 유효성 검사 오류 표시:

```kotlin
OutlinedTextField(
    value = state.email,
    onValueChange = { viewModel.onEvent(EmailChanged(it)) },
    isError = state.emailError != null,
    supportingText = state.emailError?.let { { Text(it) } }
)
```

### 비밀번호 표시 토글

```kotlin
@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle password"
                )
            }
        }
    )
}
```

### 폼 유효성 검사

제출 전 유효성 검사:

```kotlin
fun SignUpViewModel.validateEmail(email: String): EmailValidationResult {
    return when {
        email.isBlank() -> EmailValidationResult.EMPTY
        !email.contains("@") -> EmailValidationResult.INVALID_FORMAT
        else -> EmailValidationResult.VALID
    }
}

// UI 레이어에서 strings.xml 리소스로 매핑
// e.g., EmailValidationResult.EMPTY -> R.string.error_email_required
```

## AI 에이전트 안내

**auth 모듈을 수정할 때:**

1. **각 화면은 독립적입니다** - 회원가입에 영향을 주지 않고 로그인을 수정할 수 있습니다.
2. **유효성 검사는 API 호출 전에 수행됩니다** - 이메일 형식, 비밀번호 강도를 먼저 클라이언트 측에서 확인하세요.
3. **다단계 폼은 상태의 currentStep을 사용합니다** - 단계에 네비게이션 컴포저블을 사용하지 말고 상태를 사용하세요.
4. **재사용 가능한 컴포넌트는 component/에 위치합니다** - 두 화면이 같은 버튼을 사용한다면 `component/button/`에 배치하세요.
5. **오류 메시지는 strings.xml에 있습니다** - Kotlin 코드에 하드코딩하지 마세요.
6. **Hilt가 ViewModel을 제공합니다** - `@HiltViewModel`과 `hiltViewModel()` 함수를 사용하세요.
7. **인증 토큰은 안전하게 저장됩니다** - 피처 모듈이 아닌 데이터 레이어에서 처리합니다.

**피해야 할 일반적인 실수:**

- Compose 화면에 비즈니스 로직 포함 (ViewModel로 이동).
- 오류 메시지 하드코딩 (strings.xml 사용).
- 사이드 이펙트를 통한 네비게이션 (이벤트를 사용하고 부모 컴포저블이 네비게이션 처리).
- 클라이언트 측 이메일/비밀번호 유효성 검사 생략 (먼저 검사 후 API 호출).
- 다단계 폼에 네비게이션 컴포저블 사용 (상태로 단계 관리).
- 로딩 중 제출 버튼 비활성화 누락 (`enabled = !isLoading` 설정).
- 상태에 비밀번호 저장 (입력에서 받아 유즈케이스에 전달 후 폐기).

## 관련 문서

- **피처 레이어:** [../AGENTS.md](../AGENTS.md) - 일반 피처 모듈 패턴
- **도메인 레이어:** [../../domain/AGENTS.md](../../domain/AGENTS.md) - 유즈케이스 (RegisterUserUseCase 등)
- **데이터 레이어:** `data:verification` - 이메일 인증 및 비밀번호 재설정 API 호출
- **디자인 시스템:** `core/designsystem/` - 색상, 타이포그래피
- **Main 피처:** [../main/AGENTS.md](../main/AGENTS.md) - 인증 성공 후 진입점
- **네비게이션:** `domain/navigation/` - 라우트 정의
