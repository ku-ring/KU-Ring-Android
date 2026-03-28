<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# core/designsystem 모듈 안내

KU-Ring Android 앱의 중앙화된 디자인 시스템을 제공하는 모듈로, 테마, 색상, 타이포그래피, 재사용 가능한 UI 컴포넌트를 포함합니다.

## 모듈 목적

designsystem 모듈은 앱 전반의 시각적 디자인에 대한 단일 진실 공급원(Single Source of Truth)입니다. 모든 기능 모듈은 일관된 테마, 색상, 폰트, 컴포넌트 동작을 보장하기 위해 이 모듈에 의존합니다.

## 모듈 구조

### 디렉토리 레이아웃

```
designsystem/
├── src/main/
│   ├── java/com/ku_stacks/ku_ring/designsystem/
│   │   ├── kuringtheme/         ← 테마, 색상, 타이포그래피
│   │   ├── components/          ← 재사용 가능한 Compose 컴포넌트
│   │   └── utils/               ← 디자인 시스템 유틸리티
│   └── res/
│       ├── drawable*/           ← 벡터 드로어블 (다중 밀도)
│       ├── mipmap-*/            ← 앱 아이콘 (다중 밀도)
│       ├── anim/                ← 애니메이션 리소스
│       ├── values/              ← 문자열, 색상, 치수, 테마
│       ├── values-night/        ← 다크 모드 오버라이드
│       └── raw/                 ← 원시 리소스 파일
```

---

## kuringtheme 서브모듈

**목적**: 중앙화된 테마, 색상 팔레트, 타이포그래피.

### 주요 파일

#### KuringTheme.kt
- 메인 Compose 테마 함수
- 전체 서브트리에 색상과 타이포그래피 적용
- 컴포넌트 접근을 위한 `LocalColors`, `LocalTypography` 제공
- 라이트 및 다크 모드 지원

**사용법**:
```kotlin
KuringTheme {
  // 모든 자식 컴포저블은 테마 색상과 스타일을 사용합니다
}
```

#### KuringColors.kt
- 라이트 및 다크 테마를 위한 완전한 색상 팔레트
- 주요(primary), 보조(secondary), 3차(tertiary) 색상
- 의미론적 색상 (성공, 오류, 경고, 정보)
- 서피스 및 배경 색상

**패턴**:
```kotlin
val LocalColors = staticCompositionLocalOf { KuringLightColors }

data class KuringColors(
  val primary: Color,
  val primaryContainer: Color,
  val onPrimary: Color,
  // ... 기타 색상
)
```

#### KuringTypography.kt
- 모든 UI 레이어를 위한 텍스트 스타일
- 헤드라인, 제목, 본문, 레이블 스타일
- 폰트 패밀리 및 크기

**사용법**:
```kotlin
Text(
  text = "Headline",
  style = KuringTypography.headlineLarge
)
```

#### values/ 디렉토리
- `colors.xml` – 색상 리소스 정의 (Android API에서 참조)
- `themes.xml` – Material 3 통합을 위한 테마 오버레이
- `strings.xml` – 공유 문자열 리소스

---

## components 서브모듈

**목적**: KuringTheme 기반의 재사용 가능한 Compose 컴포넌트.

### 컴포넌트 구성

#### dragdrop/
- 드래그앤드롭 인터랙션 컴포넌트
- 재정렬 가능한 목록이나 드래그로 닫기 기능이 있는 기능에서 사용

#### indicator/
- **HorizontalSlidingIndicator** – 선택된 탭 아래로 슬라이드하는 애니메이션 인디케이터
- **PagingLoadingIndicator** – 페이지네이션 목록을 위한 로딩 인디케이터

#### progress/
- **CircularProgressBar** – 커스텀 원형 프로그레스 인디케이터
- 확정(determinate) 또는 비확정(indeterminate) 모드 지원

#### textfield/
- **KuringTextField** – 일관된 테마가 적용된 커스텀 텍스트 입력
- **SearchTextField** – 검색 인터랙션을 위한 특화된 텍스트 필드
- 두 컴포넌트 모두 아이콘, 오류 상태, 입력 유효성 검사 지원

#### topbar/
- **LargeTopAppBar** – 축소(collapsing) 동작이 있는 Material Design 대형 앱바
- 제목, 뒤로 가기 버튼, 액션 버튼 처리

#### 루트 컴포넌트
- **KuringAlertDialog** – 커스터마이즈된 알림 다이얼로그
- **KuringCallToAction** – 강조 효과가 있는 행동 촉구 버튼
- **KuringScrollableTabRow** – 스크롤 지원이 있는 수평 탭 행
- **KuringWebView** – 인앱 웹 콘텐츠를 위한 WebView 래퍼
- **NonLazyGrid** – 작고 스크롤되지 않는 레이아웃을 위한 그리드
- **DoubleTapBackHandler** – 두 번 탭 뒤로 가기 네비게이션을 위한 컴포저블

---

## utils 서브모듈

**목적**: 디자인 시스템 유틸리티 및 확장 함수.

### 주요 파일

#### TextExtension.kt
- `Text` 컴포저블 확장 함수
- 일반적인 텍스트 패턴을 위한 편의 함수 추가

#### Gradient.kt
- 그라디언트 유틸리티 및 브러시
- 장식용 배경과 하이라이트에 사용

#### NoRippleInteractionSource.kt
- 인터랙티브 컴포넌트에서 리플 효과 제거
- 커스텀 피드백이 선호될 때 사용

---

## res 디렉토리

**목적**: 테마 및 에셋을 위한 Android 리소스 파일.

### drawable/ (다중 밀도)
- `drawable/` – 기본 밀도
- `drawable-hdpi/` – 고밀도 (1.5x)
- `drawable-xhdpi/` – 초고밀도 (2x)
- `drawable-xxhdpi/` – 초초고밀도 (3x)
- `drawable-v24/` – API 24 이상 전용 드로어블
- **내용**: 벡터 드로어블 (SVG), 래스터 이미지, 쉐이프 정의

### mipmap/ (다중 밀도)
- `mipmap-hdpi/`, `mipmap-xhdpi/`, `mipmap-xxhdpi/`, `mipmap-xxxhdpi/`
- **내용**: 모든 밀도의 앱 런처 아이콘
- 앱 브랜딩을 위해 매니페스트에서 사용

### anim/
- 전환 효과를 위한 애니메이션 리소스
- 기능 모듈에서 사용되는 페이드, 슬라이드, 스케일 애니메이션
- Compose에서 `AnimationSpec`으로, XML에서 AnimationDrawable로 참조

### values/ & values-night/
- **colors.xml** – 색상 리소스 정의 (라이트 및 다크)
- **strings.xml** – 공유 문자열 리소스 (국제화는 app 모듈에서 처리)
- **dimens.xml** – 공통 치수 (패딩, 마진, 모서리 반경)
- **themes.xml** – Material 3 테마 오버레이
- **values-night/** – 색상 및 테마의 다크 모드 오버라이드

### raw/
- 원시 리소스 파일 (JSON, 텍스트 등)
- `context.resources.openRawResource(R.raw.filename)`으로 접근

---

## 핵심 디자인 원칙

1. **단일 진실 공급원**: 모든 시각적 디자인은 이 모듈에서 시작됩니다
2. **일관성**: 모든 색상, 폰트 크기, 컴포넌트는 디자인 시스템을 따릅니다
3. **다크 모드 지원**: `values/`와 `values-night/`를 통해 라이트/다크 테마에 색상이 적용됩니다
4. **Composition Locals**: 테마 값은 전역 상태가 아닌 Compose CompositionLocals를 통해 주입됩니다
5. **재사용성**: 컴포넌트는 일반적이며 기능에 종속되지 않습니다

---

## 일반 작업

### 새로운 색상 추가

1. `KuringColors` 데이터 클래스에 추가 (라이트 및 다크 변형)
2. `res/values/colors.xml`과 `res/values-night/colors.xml`에 리소스 추가
3. `KuringTheme`을 업데이트하여 CompositionLocal을 통해 노출
4. 이 파일에 사용법 문서화

**예시**:
```kotlin
// KuringColors.kt에서
data class KuringColors(
  val primary: Color,
  val brandNew: Color,  // 새로운 색상
)

// res/values/colors.xml에서
<color name="brand_new">#FF5500</color>

// res/values-night/colors.xml에서
<color name="brand_new">#FFB366</color>
```

### 새로운 컴포넌트 추가

1. `components/` 또는 하위 디렉토리에 파일 생성
2. 일반적이고 재사용 가능하게 만들기
3. Compose `@Preview` 함수 추가
4. `KuringTheme`을 임포트하고 테마 색상 사용
5. 라이트 및 다크 모드에서 테스트
6. 이 AGENTS.md 파일에 문서화

**예시**:
```kotlin
// components/KuringButton.kt
@Composable
fun KuringButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
      containerColor = LocalColors.current.primary,
    ),
    modifier = modifier,
  ) {
    Text(text, style = LocalTypography.current.labelLarge)
  }
}

@Preview
@Composable
private fun KuringButtonPreview() {
  KuringTheme {
    KuringButton("Click me") {}
  }
}
```

### 테마 업데이트

1. Compose 레벨 변경을 위해 `KuringTheme.kt` 수정
2. 리소스 레벨 변경을 위해 `kuringtheme/values/*` 업데이트
3. 라이트 및 다크 모드에서 모든 컴포넌트 테스트
4. 전체 테스트 스위트 실행: `./gradlew test`

---

## AI 에이전트 안내

### 작업 지침

designsystem 모듈 작업 시:

1. **일관성 유지**: 모든 변경은 앱 전체의 시각적 디자인에 영향을 줍니다.
2. **두 테마 모두 테스트**: 항상 라이트 및 다크 모드에서 컴포넌트를 테스트하세요.
3. **리소스 ID 사용**: XML에서는 `@color/brand_color`로, Compose에서는 `LocalColors.current.brandColor`로 색상을 참조하세요.
4. **미리보기 제공**: 모든 컴포넌트에는 `@Preview` 컴포저블이 있어야 합니다.
5. **공개 API 문서화**: 이 파일을 업데이트하고 새로운 컴포넌트에 KDoc 주석을 추가하세요.
6. **일반성 유지**: 컴포넌트에는 기능별 로직이 포함되어서는 안 됩니다.

### 테스트 명령어

designsystem 전용 테스트 실행:
```bash
./gradlew :core:designsystem:testDebugUnitTest
```

빌드 및 리소스 오류 확인:
```bash
./gradlew :core:designsystem:assemble
```

전체 앱 테스트 스위트 실행 (통합 이슈 감지):
```bash
./gradlew test
```

### Compose 미리보기 확인

모든 컴포넌트 미리보기가 올바르게 렌더링되는지 확인:
1. Android Studio에서 designsystem 모듈 열기
2. `@Preview` 어노테이션이 있는 컴포저블로 이동
3. IDE에서 "Preview" 클릭
4. 라이트 및 다크 모드 미리보기 확인
5. 레이아웃 오류나 잘림 없는지 검증

### 의존성 주의사항

- **기능/데이터/도메인 의존성 없음**: 이 모듈은 앱 로직과 무관하게 유지하세요
- **Kotlin stdlib 및 Compose만 사용**: 최대한의 재사용성을 위해
- **Material 3**: 해당되는 경우 Material Design 3 기본 요소 사용
- **테마 전파**: 항상 최상위에서 Compose 트리를 `KuringTheme`으로 감싸세요

### AGP 9.0 주의사항

이 모듈은 AGP 9.0과 함께 Jetpack Compose를 사용합니다. designsystem은 Android 전용 AGP 설정이 없는 순수 라이브러리 모듈이므로 특별한 마이그레이션이 필요하지 않습니다.

---

## 리소스 접근 패턴

### Compose에서
```kotlin
val colors = LocalColors.current
val typography = LocalTypography.current
```

### Android 프레임워크에서 (드로어블, 문자열)
```kotlin
val color = ContextCompat.getColor(context, R.color.brand_primary)
val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher, null)
```

---

## 문제 해결

**색상이 테마와 맞지 않는 경우**: 컴포넌트가 하드코딩된 색상 대신 `LocalColors.current`를 사용하는지 확인하세요.

**다크 모드 색상이 잘못된 경우**: `values/colors.xml`과 `values-night/colors.xml` 모두 확인하세요.

**기능 모듈에서 컴포넌트를 사용할 수 없는 경우**: designsystem 모듈에서 public으로 선언되어 있고 기능 모듈이 `:core:designsystem`에 의존하는지 확인하세요.

**미리보기가 렌더링되지 않는 경우**: `@Preview` 컴포저블이 컴포넌트를 `KuringTheme`으로 감싸고 있는지 확인하세요.
