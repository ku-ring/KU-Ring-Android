<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 도메인 레이어 모듈 안내

도메인 레이어는 모든 데이터 레이어 모듈에서 공유되는 데이터 모델, 매퍼, 기본 타입을 정의합니다. 데이터 소스와 피처 간의 계약 역할을 합니다.

## 모듈 목적

도메인 엔티티와 공유 변환을 위한 단일 진실 공급원을 제공합니다:
- **엔티티** - 핵심 비즈니스 개념을 나타내는 도메인 모델 (Notice, User, Department 등)
- **매퍼** - 도메인 모델과 다른 표현 간의 변환
- **예외** - 도메인 특화 예외 타입
- **기본 타입** - 공유 인터페이스 및 추상 클래스
- **테스트 유틸리티** - 테스트용 픽스처 및 팩토리 메서드

## 모듈 구조

```text
data/domain/
├── src/main/java/com/ku_stacks/ku_ring/domain/
│   ├── (엔티티 정의)
│   ├── mapper/
│   │   └── *Mapper.kt
│   └── (예외 클래스)
└── testUtils/
    └── (테스트 픽스처 및 팩토리)
```

## 엔티티 정의

앱의 비즈니스 모델을 나타내는 핵심 도메인 엔티티:

| 엔티티 | 목적 |
|--------|---------|
| **Notice** | 알림/공지사항 데이터 |
| **WebViewNotice** | WebView 표시용 공지사항 |
| **NoticeComment** | 공지사항 댓글 |
| **User** | 사용자 프로필 정보 |
| **Department** | 대학 학과/단과대학 |
| **Staff** | 교직원 연락처 정보 |
| **Club** | 학생 동아리/단체 |
| **ClubSummary** | 목록용 요약 동아리 정보 |
| **AcademicEvent** | 학사 일정 이벤트 |
| **LibraryRoom** | 도서관 시설 정보 |
| **Place** | 캠퍼스 건물/위치 |
| **KuringBotMessage** | AI 챗봇 메시지 및 컨텍스트 |
| **Notification** | 푸시 알림 데이터 |
| **CategoryOrder** | 커스텀 카테고리 순서 설정 |

### 엔티티 예시

```kotlin
// Notice.kt
data class Notice(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val departmentId: String,
    val postedAt: LocalDateTime,
    val imageUrl: String?,
    val link: String?,
    val isPinned: Boolean = false
)

// User.kt
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileImageUrl: String?,
    val createdAt: LocalDateTime,
    val subscribedDepartments: List<String>
)

// Club.kt
data class Club(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val memberCount: Int,
    val imageUrl: String?,
    val isSubscribed: Boolean = false
)
```

## 매퍼

도메인 모델과 다른 표현 간의 변환 함수:

### mapper/ 디렉토리

```kotlin
// mapper/NoticeMappers.kt
object NoticeMappers {
    fun NoticeResponse.toDomain(): Notice = Notice(
        id = id,
        title = title,
        content = content,
        category = category,
        departmentId = departmentId,
        postedAt = LocalDateTime.parse(postedAt),
        imageUrl = imageUrl,
        link = link
    )

    fun NoticeEntity.toDomain(): Notice = Notice(
        id = id,
        title = title,
        content = content,
        category = category,
        departmentId = departmentId,
        postedAt = LocalDateTime.ofEpochSecond(createdAt),
        imageUrl = imageUrl,
        link = link
    )

    fun Notice.toEntity(): NoticeEntity = NoticeEntity(
        id = id,
        title = title,
        content = content,
        category = category,
        departmentId = departmentId,
        createdAt = postedAt.atZone(ZoneId.systemDefault()).toInstant().epochSecond,
        imageUrl = imageUrl,
        link = link
    )
}

// 편의를 위한 확장 함수
fun NoticeResponse.toDomain(): Notice = NoticeMappers.run { toDomain() }
```

일반적인 매퍼 패턴:
- **ResponseToDomain** - API 응답 → 도메인 엔티티
- **EntityToDomain** - 데이터베이스 엔티티 → 도메인 엔티티
- **DomainToEntity** - 도메인 엔티티 → 데이터베이스 엔티티
- **DomainToRequest** - 도메인 엔티티 → API 요청

## 예외

에러 처리를 위한 도메인 특화 예외:

```kotlin
// 예외 타입
sealed class DomainException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

class NetworkException(message: String = "Network error") : DomainException(message)

class NotFoundMediaException(message: String = "Resource not found") : DomainException(message)

class UnauthorizedException(message: String = "Unauthorized") : DomainException(message)

class InvalidDataException(message: String = "Invalid data") : DomainException(message)
```

## 테스트 유틸리티 모듈

`testUtils/` 서브 모듈은 테스트용 팩토리와 픽스처를 제공합니다:

```text
data/domain/testUtils/
├── build.gradle.kts
└── src/main/java/com/ku_stacks/ku_ring/domain/testUtil/
    ├── NoticeTestFactory.kt
    ├── UserTestFactory.kt
    ├── ClubTestFactory.kt
    └── (기타 팩토리)
```

### 테스트 팩토리 예시

```kotlin
// testUtil/NoticeTestFactory.kt
object NoticeTestFactory {
    fun createNotice(
        id: String = "notice_1",
        title: String = "Test Notice",
        content: String = "Test content",
        category: String = "공지사항"
    ) = Notice(
        id = id,
        title = title,
        content = content,
        category = category,
        departmentId = "dept_1",
        postedAt = LocalDateTime.now(),
        imageUrl = null,
        link = null
    )
}

// 테스트에서 사용
@Test
fun testNoticeMapping() {
    val notice = NoticeTestFactory.createNotice(
        title = "Updated Notice"
    )
    assertEquals("Updated Notice", notice.title)
}
```

테스트 실행:

```bash
# 도메인 모듈 테스트
./gradlew :data:domain:test

# 도메인 테스트 유틸리티 (컴파일 전용)
./gradlew :data:domain:testUtils:build
```

## 의존성 모델

도메인 레이어는 모든 데이터 모듈의 의존 대상입니다:

```text
data:domain (공유 엔티티 & 매퍼)
    ↑
    사용처: notice, noticecomment, notification, user, department, club 등

data:local (Room 엔티티 & DAO)
    의존: 매핑을 위한 도메인 엔티티

data:remote (API 응답)
    의존: 매핑을 위한 도메인 엔티티

data:*/repository (구현체)
    의존: 도메인 엔티티 + 원격/로컬 소스
```

## 일반 작업

### 새 엔티티 타입 추가

1. `src/main/java/com/ku_stacks/ku_ring/domain/`에 엔티티 클래스 생성:
   ```kotlin
   data class MyEntity(
       val id: String,
       val name: String,
       val description: String,
       val createdAt: LocalDateTime
   )
   ```

2. `mapper/MyEntityMapper.kt`에 매퍼 생성:
   ```kotlin
   object MyEntityMapper {
       fun MyResponse.toDomain(): MyEntity = MyEntity(
           id = id,
           name = name,
           description = description,
           createdAt = LocalDateTime.parse(createdAt)
       )

       fun MyEntity.toResponse(): MyResponse = MyResponse(
           id = id,
           name = name,
           description = description,
           createdAt = createdAt.toString()
       )
   }

   fun MyResponse.toDomain(): MyEntity = MyEntityMapper.run { toDomain() }
   ```

3. `testUtils/MyEntityTestFactory.kt`에 테스트 팩토리 생성:
   ```kotlin
   object MyEntityTestFactory {
       fun createMyEntity(
           id: String = "entity_1",
           name: String = "Test Entity",
           description: String = "Test description"
       ) = MyEntity(
           id = id,
           name = name,
           description = description,
           createdAt = LocalDateTime.now()
       )
   }
   ```

4. `data:local/entity/MyEntity.kt`에 Room 엔티티 생성:
   ```kotlin
   @Entity(tableName = "my_entities")
   data class MyEntity(
       @PrimaryKey val id: String,
       @ColumnInfo("name") val name: String,
       // ...
   )
   ```

5. 새 도메인 엔티티를 사용하도록 데이터 모듈 매퍼 업데이트

### 엔티티 업데이트

도메인 엔티티 수정 시:

1. 엔티티 클래스에 필드 추가/삭제
2. 해당 엔티티를 참조하는 모든 매퍼 업데이트:
   - `data:remote`의 `ResponseToDomain` 매퍼
   - 레포지토리 모듈의 `EntityToDomain` 및 `DomainToEntity` 매퍼
3. `data:local/entity/`의 Room 엔티티 업데이트
4. `data:local` DatabaseModule에 Room 마이그레이션 생성
5. `testUtils/`의 테스트 팩토리 업데이트
6. `./gradlew :data:test` 실행하여 검증

### 매퍼 확장 함수 생성

매퍼는 별도 파일로 분리하고 확장 함수로 노출하세요:

```kotlin
// mapper/UserMappers.kt
object UserMappers {
    fun UserResponse.toDomain(): User = User(...)
    fun UserEntity.toDomain(): User = User(...)
    fun User.toEntity(): UserEntity = UserEntity(...)
}

// 확장 함수로 노출
fun UserResponse.toDomain(): User = UserMappers.run { toDomain() }
fun UserEntity.toDomain(): User = UserMappers.run { toDomain() }
fun User.toEntity(): UserEntity = UserMappers.run { toEntity() }

// 레포지토리에서 사용
val user = userResponse.toDomain() // 확장 함수
```

### 예외 타입 추가

도메인 레벨 에러 처리를 위한 예외 클래스 생성:

```kotlin
// 도메인 모듈에서
sealed class DomainException(message: String) : Exception(message)

class InvalidNoticeException(message: String = "Invalid notice") :
    DomainException(message)

class SubscriptionLimitExceededException(message: String = "Subscription limit exceeded") :
    DomainException(message)

// 레포지토리에서 사용
try {
    repository.subscribeToClub(clubId)
} catch (e: SubscriptionLimitExceededException) {
    showError("You have reached the maximum number of club subscriptions")
}
```

## AI 에이전트 안내

**주요 책임**:
- 도메인 엔티티는 비즈니스 로직이 없는 불변 값 객체입니다.
- 매퍼는 양방향이 모두 사용될 경우 양방향으로 구현해야 합니다.
- 테스트 팩토리는 선택적 파라미터를 통한 커스터마이징을 지원해야 합니다.
- 도메인 모듈은 데이터 소스나 프레임워크에 의존하지 않습니다 (LocalDateTime 등 제외).
- 도메인 엔티티 변경은 이를 사용하는 모든 모듈에 영향을 줍니다.

**자주 묻는 질문**:
- "동아리를 나타내는 엔티티는?" - `Club.kt`
- "매퍼는 어떻게 동작하나요?" - mapper/ 디렉토리 참고; 편의를 위해 확장 함수 사용
- "테스트 팩토리는 어디에 있나요?" - `testUtils/` 서브 모듈
- "어떤 예외를 던져야 하나요?" - sealed class 계층 구조 참고
- "새 엔티티는 어떻게 추가하나요?" - "새 엔티티 타입 추가" 섹션 참고

**테스트 방법**:
- 일관된 픽스처를 위해 `testUtils/`의 테스트 팩토리 사용
- 알려진 입력/출력으로 매퍼 함수 직접 테스트
- 모든 매퍼가 null 값을 올바르게 처리하는지 검증
- 레포지토리에서 예외 타입이 적절히 처리되는지 테스트

**주의사항**: 도메인 엔티티는 계약입니다. 변경 시 여러 모듈(remote, local, 레포지토리)에 영향을 줍니다. 항상 매퍼가 양방향으로 동작하는지 검증하고 모든 사용처를 업데이트하세요.
