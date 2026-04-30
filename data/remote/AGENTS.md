<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 원격 데이터 소스 모듈 안내

원격 데이터 소스 모듈은 중앙화된 HTTP API 클라이언트 설정과 기능 도메인별로 구성된 모든 네트워크 서비스 정의를 제공합니다. HTTP 요청에는 Ktor와 Retrofit2를 사용합니다.

## 모듈 목적

모든 피처에 대한 HTTP 클라이언트 설정과 API 서비스 인터페이스를 제공합니다. 각 서비스는 전용 Hilt DI 모듈과 함께 독립된 패키지에 분리되어 다음을 가능하게 합니다:
- API 도메인별 명확한 관심사 분리
- 피처별 독립적인 서비스 설정
- 목 서비스를 통한 간편한 테스트
- 중앙화된 에러 처리 및 로깅

## 모듈 구조

```text
data/remote/
├── src/main/java/com/ku_stacks/ku_ring/remote/
│   ├── academicevent/
│   │   ├── di/
│   │   │   └── AcademicEventModule.kt
│   │   └── response/
│   ├── club/
│   │   ├── di/
│   │   │   └── ClubModule.kt
│   │   ├── request/
│   │   └── response/
│   ├── department/
│   │   ├── di/
│   │   │   └── DepartmentModule.kt
│   │   ├── request/
│   │   └── response/
│   ├── kuringbot/
│   │   └── response/
│   ├── library/
│   │   ├── di/
│   │   │   └── LibraryModule.kt
│   │   ├── request/
│   │   └── response/
│   ├── notice/
│   │   ├── di/
│   │   │   └── NoticeModule.kt
│   │   ├── request/
│   │   └── response/
│   ├── noticecomment/
│   │   ├── di/
│   │   │   └── NoticeCommentModule.kt
│   │   ├── request/
│   │   └── response/
│   ├── report/
│   │   ├── di/
│   │   │   └── ReportModule.kt
│   │   └── request/
│   ├── space/
│   │   ├── di/
│   │   │   └── KuringSpaceModule.kt
│   │   └── response/
│   ├── staff/
│   │   ├── di/
│   │   │   └── StaffModule.kt
│   │   └── response/
│   ├── user/
│   │   ├── di/
│   │   │   └── UserModule.kt
│   │   ├── request/
│   │   └── response/
│   ├── verification/
│   │   ├── di/
│   │   │   └── VerificationModule.kt
│   │   └── request/
│   └── util/
│       ├── NetworkModule.kt
│       ├── KtorClientFactory.kt
│       └── 에러 처리/로깅 유틸리티
└── src/test/
    └── resources/
        └── api-response/ (테스트용 샘플 JSON 응답)
```

## 피처 패키지

### academicevent
**목적**: 학사 일정 및 이벤트 API

- **di/AcademicEventModule.kt** - AcademicEventService를 제공하는 Hilt 모듈
- **response/** - 응답 DTO (AcademicEventResponse, EventDetail 등)
- **서비스 인터페이스** - 날짜/카테고리별 학사 이벤트 조회 메서드

### club
**목적**: 동아리/단체 API

- **di/ClubModule.kt** - ClubService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (ClubSubscribeRequest, FilterRequest 등)
- **response/** - 응답 DTO (ClubResponse, ClubDetail 등)
- **서비스 인터페이스** - 동아리 목록 조회, 검색, 구독 메서드

### department
**목적**: 학과 정보 및 구독 API

- **di/DepartmentModule.kt** - DepartmentService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (DepartmentSubscribeRequest 등)
- **response/** - 응답 DTO (DepartmentResponse, DepartmentListResponse 등)
- **서비스 인터페이스** - 학과 정보 조회 및 구독 관리 메서드

### kuringbot
**목적**: AI 챗봇 API (서버 전송 이벤트)

- **response/** - 응답 DTO (KuringBotMessage, MessageChunk 등)
- **KuringBotSSEClient** - 스트리밍 응답을 위한 Ktor 기반 SSE 클라이언트
- **DI 모듈 없음** - KuringBotSSEClient는 직접 제공

### library
**목적**: 도서관 좌석 현황 API

- **di/LibraryModule.kt** - LibraryService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (SeatReservationRequest 등)
- **response/** - 응답 DTO (LibrarySeatsResponse, RoomAvailability 등)
- **서비스 인터페이스** - 좌석 이용 가능 여부 및 예약 조회 메서드

### notice
**목적**: 공지사항 API (앱의 주요 API)

- **di/NoticeModule.kt** - NoticeService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (NoticeFilterRequest 등)
- **response/** - 응답 DTO (NoticeResponse, NoticeListResponse, SearchNoticeResponse 등)
- **서비스 인터페이스** - 공지사항 목록 조회, 검색, 필터링 메서드

### noticecomment
**목적**: 공지사항 댓글 API

- **di/NoticeCommentModule.kt** - NoticeCommentService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (CommentRequest 등)
- **response/** - 응답 DTO (CommentResponse, CommentListResponse 등)
- **서비스 인터페이스** - 댓글 추가, 수정, 삭제 메서드

### report
**목적**: 콘텐츠 신고/피드백 API

- **di/ReportModule.kt** - ReportService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (ReportRequest, FeedbackRequest 등)
- **서비스 인터페이스** - 신고 및 피드백 제출 메서드

### space
**목적**: 스터디 공간/장소 API

- **di/KuringSpaceModule.kt** - KuringSpaceService를 제공하는 Hilt 모듈
- **response/** - 응답 DTO (SpaceResponse, VenueDetail, AppVersionResponse 등)
- **서비스 인터페이스** - 공간 이용 가능 여부 및 장소 정보 조회 메서드

### staff
**목적**: 교직원 디렉토리 API

- **di/StaffModule.kt** - StaffService를 제공하는 Hilt 모듈
- **response/** - 응답 DTO (StaffResponse, StaffMember, ContactInfo 등)
- **서비스 인터페이스** - 교직원 검색 및 목록 조회 메서드

### user
**목적**: 사용자 프로필 및 인증 API

- **di/UserModule.kt** - UserService와 FeedbackService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (UserUpdateRequest, FeedbackRequest 등)
- **response/** - 응답 DTO (UserResponse, UserListResponse, KuringBotQueryCountData 등)
- **서비스 인터페이스** - 사용자 프로필 관리 및 봇 상호작용 메서드

### verification
**목적**: 이메일 인증 API

- **di/VerificationModule.kt** - VerificationService를 제공하는 Hilt 모듈
- **request/** - 요청 DTO (VerificationRequest, CodeVerificationRequest 등)
- **서비스 인터페이스** - 이메일 인증 플로우 메서드

### util
**목적**: 공유 HTTP 클라이언트 설정 및 유틸리티

- **NetworkModule.kt** - Ktor/Retrofit 클라이언트를 제공하는 메인 Hilt 모듈
- **KtorClientFactory.kt** - Ktor HTTP 클라이언트 설정 (타임아웃, 헤더, 플러그인)
- **에러 처리** - 네트워크 에러 매퍼 및 예외 타입
- **로깅 유틸리티** - HTTP 로깅 및 디버깅 헬퍼

## DI 패턴

각 피처 패키지는 Hilt 바인딩 패턴을 따릅니다:

```kotlin
// di/MyFeatureModule.kt
@Module
@InstallIn(SingletonComponent::class)
object MyFeatureModule {
    @Provides
    @Singleton
    fun provideMyService(httpClient: HttpClient): MyService {
        return MyService(httpClient)
    }
}

// 레포지토리에서 사용
@Inject
constructor(private val service: MyService) { ... }
```

## 요청 및 응답 DTO

모든 요청과 응답은 Gson/Kotlinx Serialization을 사용하는 데이터 클래스로 정의합니다:

```kotlin
// response/NoticeResponse.kt
@Serializable
data class NoticeResponse(
    @SerialName("notice_id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("created_at")
    val createdAt: String
)

// request/NoticeFilterRequest.kt
@Serializable
data class NoticeFilterRequest(
    @SerialName("category")
    val category: String,
    @SerialName("page")
    val page: Int
)
```

## Ktor vs Retrofit

- **Ktor**는 주요 API 서비스(Notice, User, Department 등)에 사용
- **Retrofit**은 REST 특화 기능이 필요한 특정 서비스에 사용
- 두 방식 모두 동일한 DI 모듈 구조를 공유

## 테스트

테스트 리소스에는 샘플 API 응답이 포함됩니다:

```text
src/test/resources/api-response/
├── NoticeResponse.json
├── LibrarySeatsResponse.json
├── UserListResponse.json
├── DepartmentNoticeResponse.json
└── DefaultResponse.json
```

테스트 실행:

```bash
# 원격 모듈 테스트
./gradlew :data:remote:test

# 특정 서비스 테스트
./gradlew :data:remote:test --tests "*NoticeServiceTest"
```

픽스처를 사용한 서비스 목 처리:

```kotlin
// 테스트에서
val mockResponse = NoticeResponse(...)
val mockService = mock<NoticeService> {
    onBlocking { getNotices(any()) } doReturn mockResponse
}
```

## 일반 작업

### 새 API 서비스 추가

1. 피처 패키지 생성: `remote/myfeature/`
2. 서브 디렉토리 생성:
   ```text
   di/
   request/
   response/
   ```
3. `response/`에 응답 DTO 정의:
   ```kotlin
   @Serializable
   data class MyResponse(
       @SerialName("field_name")
       val fieldName: String
   )
   ```
4. 서비스 인터페이스 정의 (Retrofit):
   ```kotlin
   interface MyService {
       @GET("/api/my-endpoint")
       suspend fun getData(): MyResponse
   }
   ```
5. `di/MyModule.kt`에 Hilt 모듈 생성:
   ```kotlin
   @Module
   @InstallIn(SingletonComponent::class)
   object MyModule {
       @Provides
       @Singleton
       fun provideMyService(@Default retrofit: Retrofit): MyService {
           return retrofit.create(MyService::class.java)
       }
   }
   ```
6. `src/test/resources/api-response/MyResponse.json`에 샘플 응답으로 테스트

### API 응답 업데이트

1. `feature/response/`의 응답 DTO 수정
2. 실제 API 필드명과 일치하도록 모든 @SerialName 어노테이션 업데이트
3. 테스트 리소스의 샘플 JSON 업데이트
4. 역직렬화 검증을 위해 `./gradlew :data:remote:test` 실행
5. 의존하는 데이터 모듈의 레포지토리 매퍼 업데이트

### HTTP 클라이언트 설정

`util/NetworkModule.kt`와 `KtorClientFactory.kt` 수정:

```kotlin
// 전역 헤더 추가
val httpClient = HttpClient {
    defaultRequest {
        header("User-Agent", "KuRing/2.4.3")
    }
}

// 로깅 추가
install(Logging) {
    level = LogLevel.BODY
}

// 타임아웃 설정
install(HttpTimeout) {
    socketTimeoutMillis = 30000
}
```

### API 에러 처리

에러 응답 DTO와 매퍼 생성:

```kotlin
@Serializable
data class ErrorResponse(
    @SerialName("error_code")
    val code: String,
    @SerialName("message")
    val message: String
)

// 레포지토리에서
try {
    service.getNotices()
} catch (e: HttpRequestTimeoutException) {
    throw NetworkTimeoutException()
} catch (e: ResponseException) {
    throw ApiException(e.response.status.description)
}
```

## AI 에이전트 안내

**주요 책임**:
- 서비스는 상태 없는 HTTP 클라이언트이며, DI 모듈이 싱글톤을 제공합니다.
- 응답 DTO는 API 계약과 정확히 일치해야 합니다 (필드 매핑에 @SerialName 사용).
- HTTP 메서드가 아닌 피처 패키지 단위로 새 서비스를 추가하세요.
- 요청/응답 DTO는 단순하게 유지하고, 복잡한 로직은 레포지토리 매퍼에서 처리하세요.
- 샘플 JSON 응답으로 테스트하고 `./gradlew :data:remote:test`로 역직렬화를 검증하세요.

**자주 묻는 질문**:
- "공지사항 API 서비스는 어디에 있나요?" - `remote/notice/di/NoticeModule.kt`
- "요청 DTO는 어떻게 추가하나요?" - `feature/request/`에 생성, @Serializable과 @SerialName 사용
- "HTTP 클라이언트는 어떻게 설정하나요?" - `util/NetworkModule.kt` 수정
- "Ktor와 Retrofit의 차이는 무엇인가요?" - "Ktor vs Retrofit" 섹션 참고
- "API 응답 테스트는 어떻게 하나요?" - `src/test/resources/api-response/`에 JSON 파일 추가 후 서비스 목 처리

**주의사항**: API 계약 변경은 여러 레포지토리에 영향을 줍니다. 응답 DTO 업데이트 전 반드시 `./gradlew :data:remote:test`로 테스트하세요.
