<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 데이터 레이어 모듈 안내

데이터 레이어는 KU-Ring Android 앱의 모든 로컬 및 원격 데이터 소스를 관리하고 레포지토리 인터페이스를 구현합니다. 도메인 레이어(비즈니스 로직)와 외부 서비스(API, 데이터베이스) 사이에 위치합니다.

## 모듈 목적

레포지토리를 통해 데이터 접근 추상화를 제공합니다. 각 레포지토리 구현체는:
- 원격(API)과 로컬(데이터베이스) 데이터 소스를 조율
- 데이터 변환 처리 (응답 → 도메인 엔티티)
- 페이징, 캐싱, 에러 처리 구현
- 의존성 주입에 Hilt 사용

## 모듈 구조

```
data/
├── domain/                 # 공유 도메인 모델, 매퍼, 예외
├── local/                  # Room 데이터베이스, DAO, 엔티티, 타입 컨버터
├── remote/                 # HTTP 클라이언트, API 서비스 (Ktor/Retrofit)
├── notice/                 # 공지사항 레포지토리 구현
├── noticecomment/          # 공지사항 댓글 레포지토리 구현
├── notification/           # 푸시 알림 레포지토리 구현
├── academicevent/          # 학사 일정 레포지토리 구현
├── ai/                     # AI/KuringBot 레포지토리 구현
├── user/                   # 사용자 프로필 레포지토리 구현
├── staff/                  # 교직원 디렉토리 레포지토리 구현
├── department/             # 학과 정보 레포지토리 구현
├── library/                # 도서관 좌석 현황 레포지토리 구현
├── place/                  # 캠퍼스 장소/건물 레포지토리 구현
├── report/                 # 콘텐츠 신고 레포지토리 구현
├── space/                  # 스터디 공간 레포지토리 구현
├── search/                 # 검색 기능 레포지토리 구현
├── verification/           # 이메일 인증 레포지토리 구현
└── club/                   # 동아리/단체 레포지토리 구현
```

## 데이터 모듈 개요

| 모듈 | 목적 | 주요 구성요소 |
|--------|---------|-----------------|
| **domain** | 공유 도메인 모델, 매퍼, 엔티티 정의 | 모델(Notice, User 등), 매퍼, 예외, 기본 타입 |
| **domain:testUtils** | 테스트 픽스처 및 팩토리 메서드 | 테스트용 도메인 모델 팩토리 |
| **local** | Room 데이터베이스, DAO, 엔티티 | Database 클래스, DAO 인터페이스, 엔티티 클래스, 타입 컨버터 |
| **local:test** | 로컬 데이터베이스 테스트 유틸리티 | 테스트용 인메모리 데이터베이스 설정 |
| **remote** | 중앙화된 API 클라이언트 모듈 | HTTP 서비스, 요청/응답 모델 (기능별 구성) |
| **notice** | 공지사항 레포지토리 구현 | NoticeRepositoryImpl, 페이징 미디에이터, 응답/엔티티 매퍼 |
| **notice:test** | 공지사항 테스트 유틸리티 | 테스트 픽스처, 목 미디에이터 |
| **noticecomment** | 공지사항 댓글 레포지토리 | CommentRepositoryImpl, 응답/엔티티 매퍼 |
| **notification** | 푸시 알림 레포지토리 | NotificationRepositoryImpl, 로컬 데이터소스 |
| **academicevent** | 학사 일정 레포지토리 | EventRepositoryImpl, 응답 매퍼 |
| **ai** | KuringBot/AI 레포지토리 | BotRepositoryImpl, 메시지 처리 |
| **user** | 사용자 프로필 레포지토리 | UserRepositoryImpl, 프로필 매퍼 |
| **staff** | 교직원 디렉토리 레포지토리 | StaffRepositoryImpl, 디렉토리 검색 |
| **department** | 학과 정보 레포지토리 | DepartmentRepositoryImpl, 구독 처리 |
| **department:test** | 학과 테스트 유틸리티 | 목 레포지토리 |
| **library** | 도서관 좌석 현황 | LibraryRepositoryImpl, 좌석 상태 매퍼 |
| **place** | 캠퍼스 장소/건물 레포지토리 | PlaceRepositoryImpl, 로컬 에셋 로딩 |
| **report** | 콘텐츠 신고 레포지토리 | ReportRepositoryImpl, 피드백 제출 |
| **space** | 스터디 공간 레포지토리 | SpaceRepositoryImpl, 이용 가능 데이터 |
| **search** | 검색 기능 | SearchRepositoryImpl, 모듈 간 검색 |
| **verification** | 이메일 인증 레포지토리 | VerificationRepositoryImpl, 인증 플로우 |
| **club** | 동아리/단체 레포지토리 | ClubRepositoryImpl, 페이징 지원 |

## 레포지토리 패턴

모든 레포지토리는 표준 구현 패턴을 따릅니다:

```kotlin
// di/RepositoryModule.kt - Hilt @Binds 패턴
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindNoticeRepository(impl: NoticeRepositoryImpl): NoticeRepository
}

// repository/NoticeRepositoryImpl.kt - 데이터 소스 조율 구현
@Inject
constructor(
    private val remoteDataSource: NoticeRemoteDataSource,
    private val localDataSource: NoticeLocalDataSource,
    private val mapper: NoticeResponseToDomainMapper
) : NoticeRepository {
    override fun getNotices(): Flow<PagingData<Notice>> {
        // 원격과 로컬 소스 조율
    }
}

// mapper/ - 데이터 변환 레이어
// ResponseToDomain - API 응답 → 도메인 엔티티
// EntityToDomain - 데이터베이스 엔티티 → 도메인 엔티티
// DomainToEntity - 도메인 엔티티 → 데이터베이스 엔티티
// ResponseToEntity - API 응답 → 데이터베이스 엔티티
```

## Remote 모듈 구성

`data:remote` 모듈은 API 도메인별로 중앙화되어 구성됩니다:

| 서브 패키지 | 목적 |
|-------------|---------|
| academicevent/ | 학사 일정 API (di/, response/) |
| club/ | 동아리/단체 API (di/, request/, response/) |
| department/ | 학과 정보 API (di/, request/, response/) |
| kuringbot/ | AI 챗봇 API (response/) |
| library/ | 도서관 좌석 현황 API (di/, request/, response/) |
| notice/ | 공지사항 API (di/, request/, response/) |
| noticecomment/ | 공지사항 댓글 API (di/, request/, response/) |
| report/ | 콘텐츠 신고 API (di/, request/) |
| space/ | 스터디 공간 API (di/, response/) |
| staff/ | 교직원 디렉토리 API (di/, response/) |
| user/ | 사용자 API (di/, request/, response/) |
| verification/ | 이메일 인증 API (di/, request/) |
| util/ | 공유 네트워킹 유틸리티 (Ktor/Retrofit 설정, 에러 처리) |

각 서브 패키지에는 해당 HTTP 서비스를 제공하는 Hilt DI 모듈이 포함됩니다.

## Local 모듈 구성

`data:local` 모듈은 Room 데이터베이스를 관리합니다:

- **di/** - Database 및 DAO 인스턴스를 제공하는 Hilt 모듈
- **room/** - Database 클래스와 DAO 인터페이스
- **entity/** - @Entity 어노테이션이 적용된 Room 엔티티 클래스
- **typeconverter/** - 커스텀 타입을 위한 Room TypeConverter
- **test/** - 인메모리 테스트 데이터베이스 설정

## Domain 모듈 구성

`data:domain` 모듈은 공유 모델을 제공합니다:

- **mapper/** - 도메인 엔티티와 데이터 레이어 모델 간 변환
- **testUtils/** - 테스트 픽스처 및 팩토리 메서드

## 주요 패턴

### 페이징 구현

`notice`, `club` 등의 모듈은 원격/로컬 조율에 `PagingMediator`를 사용합니다:

```kotlin
// notice/source/CategoryNoticeMediator.kt
class CategoryNoticeMediator(
    private val remoteDataSource: NoticeRemoteDataSource,
    private val localDataSource: NoticeLocalDataSource
) : RemoteMediator<Int, NoticeEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NoticeEntity>
    ): MediatorResult { ... }
}
```

### 타입 컨버터

Room에 저장되는 커스텀 타입은 TypeConverter를 사용합니다:

```kotlin
// local/typeconverter/
@TypeConverter
fun fromList(value: List<String>): String = Json.encodeToString(value)

@TypeConverter
fun toList(value: String): List<String> = Json.decodeFromString(value)
```

### 데이터 소스 분리

테스트 용이성을 위해 원격과 로컬 소스를 분리합니다:

```kotlin
interface NoticeRemoteDataSource {
    suspend fun getNotices(page: Int): NoticeListResponse
}

interface NoticeLocalDataSource {
    fun observeNotices(): Flow<List<NoticeEntity>>
    suspend fun insertNotices(entities: List<NoticeEntity>)
}
```

## 테스트

특정 데이터 모듈 테스트 실행:

```bash
# 공지사항 모듈 테스트
./gradlew :data:notice:test

# 로컬 데이터베이스 테스트
./gradlew :data:local:test

# 학과 테스트
./gradlew :data:department:test

# 전체 데이터 레이어 테스트
./gradlew :data:test
```

테스트 모듈은 `data:domain:testUtils`의 픽스처와 `data:local:test`의 인메모리 데이터베이스를 사용합니다.

## 의존성

데이터 모듈의 의존성:
- **domain/** - 레포지토리 인터페이스와 엔티티 정의
- **data:remote/** - HTTP API 클라이언트 제공
- **data:local/** - Room 데이터베이스 DAO 제공
- **data:domain/** - 공통 매퍼와 모델 공유
- **core/** 모듈들 - 유틸리티 (core:util, core:preferences)

피처는 레포지토리 인터페이스를 통해 데이터 모듈에 의존합니다.

## 일반 작업

### 새 레포지토리 추가

1. 모듈 생성: `data/mynewfeature/`
2. 구조 생성:
   ```
   src/main/java/com/ku_stacks/ku_ring/mynewfeature/
   ├── di/RepositoryModule.kt
   ├── mapper/
   │   ├── ResponseToDomainMapper.kt
   │   └── EntityToDomainMapper.kt
   ├── repository/
   │   ├── MyFeatureRepository.kt
   │   └── MyFeatureRepositoryImpl.kt
   └── source/
       ├── RemoteDataSource.kt
       └── LocalDataSource.kt
   ```
3. `di/RepositoryModule.kt`에 Hilt 바인딩 추가
4. 소스 조율과 함께 `*RepositoryImpl` 구현
5. `data:remote` 모듈에 원격 서비스 추가
6. `data:domain:testUtils`의 픽스처를 사용하는 테스트 모듈 추가

### 도메인 모델 업데이트

도메인 모델 업데이트 시:
1. `data:domain/`에서 모델 수정
2. 영향받는 레포지토리 모듈의 모든 매퍼 업데이트
3. `data:local/`의 Room 엔티티와 타입 컨버터 업데이트
4. `app/schemas/`에 Room 마이그레이션 생성
5. `./gradlew :data:test`로 테스트

### 원격 API 서비스 추가

1. `data:remote/src/main/java/com/ku_stacks/ku_ring/remote/myfeature/`에 피처 패키지 생성
2. 구조 생성:
   ```
   di/
   ├── MyFeatureModule.kt
   request/
   response/
   ```
3. Ktor 또는 Retrofit 어노테이션으로 서비스 인터페이스 정의
4. 요청/응답 DTO 생성
5. 서비스를 제공하는 Hilt 모듈 추가
6. `./gradlew :data:remote:test`로 테스트

## AI 에이전트 안내

**주요 책임**:
- 레포지토리는 도메인 인터페이스를 구현합니다. 레포지토리 수정 전에 도메인 모듈 인터페이스를 먼저 읽으세요.
- 데이터 변환(매퍼)은 양방향이 모두 필요한 경우 양방향으로 구현해야 합니다.
- 페이징 구현은 무한 루프나 데이터 누락을 방지하기 위해 미디에이터 로직에 주의가 필요합니다.
- Room 스키마 변경은 마이그레이션이 필요하며 `./gradlew :app:build`로 테스트하세요.
- 원격 API 변경은 여러 레포지토리에 영향을 줄 수 있으므로 응답 모델 사용처를 검색하세요.

**자주 묻는 질문**:
- "페이징은 어떻게 동작하나요?" - `notice:source/CategoryNoticeMediator.kt` 참고 (RemoteMediator 패턴)
- "Room 엔티티는 어디에 있나요?" - `data:local/entity/`
- "새 레포지토리는 어떻게 추가하나요?" - 위의 "새 레포지토리 추가" 섹션 참고
- "API 서비스는 어디에 있나요?" - `data:remote/{feature}/di/`
- "매퍼는 어떻게 동작하나요?" - 레포지토리 모듈의 mapper/ 디렉토리 참고

**테스트 방법**:
- 단위 테스트는 `data:local:test`의 인메모리 데이터베이스 사용
- `data:domain:testUtils`의 픽스처로 원격 데이터소스 목 처리
- `TestRemoteMediator` 헬퍼로 미디에이터 별도 테스트
- 속성 단언문으로 매퍼 정확성 검증

**주의사항**: 데이터 레이어는 핵심 인프라입니다. 변경 시 모든 피처에 영향을 줍니다. 커밋 전 반드시 `./gradlew :data:test`로 테스트하세요.
