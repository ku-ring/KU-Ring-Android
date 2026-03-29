<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# core/ui 모듈 안내

앱 전반의 공통 도메인을 위한 공유 Compose UI 컴포넌트를 제공하는 모듈입니다. 모든 기능 모듈은 시각적 일관성을 유지하고 코드 중복을 줄이기 위해 이 컴포넌트들을 사용합니다.

## 모듈 목적

ui 모듈은 designsystem보다 상위 수준의 추상화입니다. designsystem이 기본 컴포넌트와 테마를 제공한다면, ui는 동아리, 댓글, 학과, 공지사항을 위한 도메인 특화적이면서 기능에 종속되지 않는 컴포넌트를 제공합니다.

## 모듈 구조

```
ui/
├── src/main/
│   ├── java/com/ku_stacks/ku_ring/ui/
│   │   ├── club/              ← 동아리 관련 UI 컴포넌트
│   │   ├── comment/           ← 댓글 표시 컴포넌트
│   │   ├── department/        ← 학과 관련 UI
│   │   ├── notice/            ← 공지사항 목록 아이템
│   │   └── preview/           ← 미리보기 데이터 프로바이더
│   └── res/values/
│       └── strings.xml        ← 공유 문자열 리소스
├── src/debug/
│   └── java/com/ku_stacks/ku_ring/ui/
│       └── club/              ← 디버그 전용 동아리 UI
└── build.gradle.kts
```

---

## club/ 디렉토리

**목적**: 동아리 관련 Compose UI 컴포넌트 및 유틸리티.

### 주요 컴포넌트

#### ClubItemCard
- 단일 동아리를 카드 형태로 표시
- 동아리 이름, 카테고리, 설명, 이미지 표시
- 동아리 목록 화면에서 사용
- 클릭 핸들러 및 선택 상태 지원

#### ClubTag
- 동아리 카테고리를 위한 소형 칩/태그 컴포넌트
- 동아리 카드 내부 또는 독립적으로 사용
- 카테고리 기반의 의미론적 색상 적용

#### ClubListSortButtonRow
- 동아리 목록 정렬을 위한 버튼 행
- 버튼: 가나다순, 최신순, 인기순
- 정렬 상태 및 콜백 관리

#### ClubItemColumn
- 동아리 아이템의 수직 배열
- 페이지네이션 목록을 위한 LazyColumn 통합
- 비어 있는 상태 및 로딩 처리

#### ClubSortOption
- 정렬 순서를 정의하는 열거형 또는 sealed class
- ClubListSortButtonRow에서 사용
- 옵션: NAME_ASC, NAME_DESC, NEWEST, POPULAR

**Debug 소스 셋** (`src/debug/java/`):
- 디버그 전용 동아리 UI 컴포넌트 (예: 가짜 데이터 미리보기)
- 프로토타이핑을 위해 디버그 빌드에서만 사용

**사용 예시**:
```kotlin
LazyColumn {
  items(clubs) { club ->
    ClubItemCard(
      club = club,
      onClubClick = { /* 네비게이션 */ },
    )
  }
}
```

---

## comment/ 디렉토리

**목적**: 댓글 표시 및 포맷팅.

### 주요 컴포넌트

#### Comment
- 단일 댓글 컴포저블
- 작성자, 타임스탬프, 내용 표시
- 답글 및 스레딩 지원
- 해당되는 경우 리치 텍스트 포맷팅 처리

**사용 예시**:
```kotlin
Comment(
  comment = commentData,
  onReplyClick = { /* 답글 다이얼로그 열기 */ },
  onDeleteClick = { /* 댓글 삭제 */ },
)
```

---

## department/ 디렉토리

**목적**: 학과 관련 UI 컴포넌트.

### 주요 컴포넌트

#### DepartmentItems
- 학과 아이템의 목록 또는 그리드
- 학과 선택 화면에서 사용
- 단일/다중 선택 지원

**사용 예시**:
```kotlin
DepartmentItems(
  departments = allDepartments,
  selectedDepartments = userSelectedDepartments,
  onSelectionChange = { selected -> /* 상태 업데이트 */ },
)
```

---

## notice/ 디렉토리

**목적**: 공지사항 목록 아이템 컴포넌트 및 페이지네이션 지원.

### 주요 컴포넌트

#### NoticeItem
- 단일 공지사항 목록 아이템
- 제목, 학과, 날짜, 미리보기 표시
- 클릭, 즐겨찾기, 공유 액션 지원
- 새 알림/안 읽음 상태 표시

#### LazyPagingNoticeItemColumn
- 페이지네이션된 공지사항 목록을 위한 LazyColumn 래퍼
- Paging3 라이브러리와 통합
- 로딩, 오류, 비어 있는 상태 처리
- 추가 로드(append) 및 새로고침(refresh) 동작

**사용 예시**:
```kotlin
LazyPagingNoticeItemColumn(
  pagingNotices = noticesPager,
  onNoticeClick = { notice -> /* 네비게이션 */ },
  onFavoriteClick = { notice -> /* 즐겨찾기 토글 */ },
  modifier = Modifier.fillMaxSize(),
)
```

**페이지네이션 통합**:
```kotlin
val noticesPager = remember {
  Pager(
    config = PagingConfig(pageSize = 20),
    pagingSourceFactory = { noticePagingSource },
  ).flow.cachedIn(viewModelScope)
}
```

---

## preview/ 디렉토리

**목적**: Compose 미리보기 및 테스트를 위한 미리보기 데이터 프로바이더.

### 주요 파일

#### NoticePreviewData.kt
- `@Preview` 컴포저블을 위한 샘플 공지사항 데이터
- 현실적인 공지사항 제목, 학과, 날짜
- 다양한 변형 (짧은 것, 긴 것, 이미지 포함 등)

#### DepartmentPreviewData.kt
- 샘플 학과 데이터
- 앱에서 사용되는 모든 학과 카테고리

#### 기타 미리보기 데이터
- ClubPreviewData, CommentPreviewData 등
- 정리를 위해 도메인별 파일 구성

**사용 예시**:
```kotlin
@Preview
@Composable
private fun NoticeItemPreview() {
  KuringTheme {
    NoticeItem(notice = NoticePreviewData.sampleNotice)
  }
}
```

---

## res/values 디렉토리

**목적**: UI 컴포넌트를 위한 공유 문자열 리소스.

### strings.xml
- 공유 UI 컴포넌트에서 사용되는 문자열 상수
- 동아리 관련 문자열: "동아리", "전체 동아리", "카테고리별 필터"
- 공지사항 관련 문자열: "공지사항 없음", "공유", "즐겨찾기"
- 댓글 관련 문자열: "답글", "댓글 삭제"
- 학과 문자열: "학과 선택"
- 공통 액션 문자열: "뒤로", "저장", "취소", "로딩 중", "오류"

**패턴**:
```xml
<!-- 동아리 문자열 -->
<string name="club_title">Club</string>
<string name="club_all">All Clubs</string>
<string name="club_filter_category">Filter by Category</string>

<!-- 공지사항 문자열 -->
<string name="notice_empty">No notices found</string>
<string name="notice_share">Share</string>
<string name="notice_favorite">Add to favorites</string>
```

**현지화 참고사항**: 언어별 번역은 언어 전용 `values-` 디렉토리의 `strings.xml`을 업데이트하세요 (예: 한국어는 `values-ko/`).

---

## 의존성

**의존 대상**:
- `:core:designsystem` – 테마 및 기본 컴포넌트
- Jetpack Compose 라이브러리
- Paging3 라이브러리 (`LazyPagingNoticeItemColumn` 용)

**의존하는 모듈**:
- 모든 기능 모듈 (동아리, 공지사항, 댓글 등)

---

## 핵심 디자인 원칙

1. **기능 독립성**: 컴포넌트는 ViewModel이나 네비게이션을 알지 못합니다
2. **데이터 주도**: 데이터를 매개변수로 받고, 콜백을 통해 이벤트를 전달합니다
3. **Composable 우선**: 모든 컴포넌트는 `@Composable` 함수입니다
4. **미리보기 지원**: 모든 컴포넌트에는 `@Preview` 함수가 있습니다
5. **재사용성**: 하드코딩된 문자열 없음; 문자열 리소스 또는 매개변수로 전달합니다
6. **테마 인식**: 모두 designsystem의 `LocalColors`와 `LocalTypography`를 사용합니다

---

## 일반 작업

### 새로운 도메인 컴포넌트 추가

1. `ui/` 아래에 없으면 디렉토리 생성 (예: `event/`)
2. 해당 디렉토리에 컴포저블 파일 생성
3. `@Preview` 컴포저블 추가
4. `preview/` 디렉토리에 `Preview{Domain}Data.kt` 생성
5. `res/values/strings.xml`에 새로운 문자열 리소스 추가
6. 이 AGENTS.md 파일에 문서화

**예시**:
```kotlin
// ui/event/EventItemCard.kt
@Composable
fun EventItemCard(
  event: Event,
  onEventClick: (Event) -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier.clickable { onEventClick(event) },
  ) {
    Column(Modifier.padding(16.dp)) {
      Text(event.title, style = LocalTypography.current.headlineMedium)
      Text(event.date, style = LocalTypography.current.bodySmall)
    }
  }
}

@Preview
@Composable
private fun EventItemCardPreview() {
  KuringTheme {
    EventItemCard(event = EventPreviewData.sampleEvent, onEventClick = {})
  }
}
```

### 새로운 문자열 리소스 추가

1. `res/values/strings.xml` 열기
2. 설명적인 이름으로 새로운 `<string>` 요소 추가
3. 현지화된 버전은 `values-{locale}/strings.xml`에 추가
4. 코드에서 `stringResource(R.string.my_new_string)`으로 참조

**예시**:
```xml
<string name="event_title">Event</string>
<string name="event_view_details">View Details</string>
```

### 기능 모듈에서 컴포넌트 통합

1. 기능의 `build.gradle.kts`에 의존성 추가:
   ```kotlin
   dependencies {
     implementation(projects.core.ui)
   }
   ```
2. 컴포넌트 임포트:
   ```kotlin
   import com.ku_stacks.ku_ring.ui.notice.NoticeItem
   ```
3. 컴포저블에서 사용:
   ```kotlin
   NoticeItem(notice = notice, onNoticeClick = { /* 처리 */ })
   ```

---

## AI 에이전트 안내

### 작업 지침

ui 모듈 작업 시:

1. **공유 목적 유지**: 2개 이상의 기능에서 실제로 재사용되는 컴포넌트만 추가하세요
2. **관심사 분리**: UI 로직 (외관) vs 비즈니스 로직 (네비게이션, 상태)
3. **콜백 제공**: 기능이나 네비게이션을 직접 임포트하지 말고 람다를 사용하세요
4. **미리보기 테스트**: 모든 컴포넌트에 작동하는 `@Preview`가 있어야 합니다
5. **미리보기 데이터 사용**: 하드코딩된 값이 아닌 `preview/` 패키지의 샘플 데이터를 임포트하세요
6. **매개변수 문서화**: 컴포넌트 의도와 콜백을 설명하는 KDoc 주석을 추가하세요
7. **문자열 관리**: 모든 사용자에게 표시되는 텍스트는 `strings.xml`에, 하드코딩 금지

### 테스트 명령어

ui 모듈 테스트 실행:
```bash
./gradlew :core:ui:testDebugUnitTest
```

빌드 및 검증:
```bash
./gradlew :core:ui:assemble
```

모든 기능이 여전히 빌드되는지 확인 (ui는 의존성이므로):
```bash
./gradlew :feature:*:assemble
```

### Compose 미리보기 확인

모든 컴포넌트 미리보기가 렌더링되는지 확인:
1. Android Studio에서 ui 모듈 열기
2. `@Preview`가 있는 파일로 이동
3. IDE에서 "Preview" 창 클릭
4. 모든 미리보기가 오류 없이 렌더링되는지 확인
5. 라이트 및 다크 테마 모두 확인

### 새로운 기능 도메인 추가

새로운 기능 도메인이 도입될 때 (예: 이벤트, 피드백):

1. **기능 팀과 협의**: 어떤 컴포넌트가 필요한지 파악
2. **도메인 디렉토리 생성**: `ui/{domain}/`
3. **컴포넌트 설계**: 일반적으로 도메인당 1-2개의 재사용 가능한 컴포넌트
4. **미리보기 데이터 추가**: `Preview{Domain}Data.kt` 생성
5. **문서화**: 새로운 섹션으로 이 AGENTS.md 업데이트
6. **통합 테스트**: 기능 모듈이 임포트하여 사용할 수 있는지 확인

### 의존성 주의사항

- **기능 의존성 없음**: ui는 기능, 데이터, 도메인 모듈에 의존해서는 안 됩니다
- **Designsystem만**: 핵심 의존성은 `:core:designsystem`만 있어야 합니다
- **ViewModel/Navigation 없음**: 컴포넌트는 데이터를 받고 콜백으로 전달; 기능 모듈이 네비게이션을 처리합니다
- **Hilt 주입 없음**: 컴포넌트는 모든 입력을 매개변수로 받아야 합니다 (기능이 화면 레벨에서 주입)

### Debug 소스 셋

`src/debug/` 디렉토리에는 디버그 전용 UI 컴포넌트가 있습니다:

- 빠른 프로토타이핑이나 기능 탐색에 사용
- 릴리스 빌드에 포함되지 않음
- 예시: 하드코딩된 샘플 데이터가 있는 동아리 UI 컴포넌트
- 메인 소스 셋에서 디버그 컴포넌트를 참조하지 마세요

---

## 문제 해결

**기능에서 컴포넌트를 찾을 수 없는 경우**: 기능이 `:core:ui`에 의존하고 모듈이 빌드되었는지 확인하세요.

**문자열 리소스를 찾을 수 없는 경우**: `strings.xml`에 항목이 있는지 확인하고 프로젝트를 다시 빌드하세요 (`./gradlew clean build`).

**미리보기가 렌더링되지 않는 경우**: 컴포넌트를 `KuringTheme { ... }`으로 감싸고 `@Preview` 어노테이션이 있는지 확인하세요.

**다크 모드 색상이 잘못된 경우**: 컴포넌트가 하드코딩된 색상 대신 `LocalColors.current`를 사용하는지 확인하세요.

**페이지네이션이 작동하지 않는 경우**: 기능이 `collectAsLazyPagingItems()`를 사용하고 `LazyPagingNoticeItemColumn`이 올바른 Flow를 받고 있는지 확인하세요.

---

## 관련 모듈

- **:core:designsystem** – 기본 컴포넌트, 색상, 타이포그래피
- **:feature:notice** – `NoticeItem`, `LazyPagingNoticeItemColumn` 사용
- **:feature:club** – `ClubItemCard`, `ClubListSortButtonRow` 사용
- **:feature:comment** – `Comment` 사용
