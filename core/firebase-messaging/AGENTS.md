<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# core/firebase-messaging 모듈 안내

KU-Ring 앱의 푸시 알림 처리를 위한 Firebase Cloud Messaging (FCM) 통합 모듈입니다.

## 모듈 목적

firebase-messaging 모듈은 Firebase Cloud Messaging으로부터 수신되는 푸시 알림을 관리하고, 도메인 모델로 매핑하며, 적절한 앱 응답(알림 표시, 데이터 저장, 애널리틱스 이벤트)을 트리거합니다.

## 모듈 구조

```text
firebase-messaging/
├── src/main/
│   └── java/com/ku_stacks/ku_ring/firebase/messaging/
│       ├── KuringMessagingService.kt          ← FCM 메시지 핸들러
│       ├── FcmUtil.kt                         ← FCM 유틸리티
│       ├── di/
│       │   └── FirebaseMessageModule.kt       ← Hilt DI 설정
│       ├── mapper/
│       │   └── DataToEntity.kt                ← FCM 데이터 → 도메인 엔티티 매핑
│       └── type/
│           └── NotificationType.kt            ← 알림 타입 열거형
├── src/test/
│   └── java/...                               ← 단위 테스트
└── build.gradle.kts
```

---

## KuringMessagingService

**목적**: 수신되는 FCM 메시지를 처리하기 위한 `FirebaseMessagingService` 확장.

### 책임

1. **수신 메시지 처리**: `onMessageReceived(remoteMessage: RemoteMessage)`
2. **도메인 모델로 매핑**: `DataToEntity.mapFcmData()` 변환기 사용
3. **알림 표시**: Android 알림 생성 및 게시
4. **데이터 저장**: 메시지 데이터를 로컬 데이터베이스 또는 환경설정에 저장
5. **이벤트 추적**: Firebase Analytics에 로그 기록

### 주요 메서드

#### onMessageReceived(remoteMessage: RemoteMessage)
데이터 메시지 수신 시 호출됩니다(포그라운드/백그라운드 포함). KU-Ring은 데이터 전용 메시지를 사용하므로 앱 상태와 무관하게 이 메서드가 호출됩니다.

**흐름**:
```text
FCM 메시지 도착
  ↓
KuringMessagingService.onMessageReceived()
  ↓
데이터 페이로드 추출
  ↓
DataToEntity.mapFcmData() → 도메인 모델
  ↓
데이터베이스/환경설정에 저장
  ↓
알림 생성
  ↓
NotificationManager에 게시
  ↓
Analytics에 이벤트 로그 기록
```

**구현 패턴**:
```kotlin
override fun onMessageReceived(remoteMessage: RemoteMessage) {
  val type = remoteMessage.data["type"] ?: return
  val notificationType = NotificationType.fromString(type)

  val entity = DataToEntity.mapFcmData(remoteMessage.data, notificationType)

  // 데이터베이스에 저장
  noticeRepository.saveNotice(entity)

  // 알림 표시
  showNotification(entity)

  // 이벤트 추적
  analytics.logNewNotification(notificationType)
}
```

#### onNewToken(token: String)
FCM이 새로운 기기 토큰을 생성할 때 (앱 설치, 토큰 갱신 시) 호출됩니다.

**책임**:
- 타겟팅을 위해 백엔드에 토큰 저장
- 로컬 접근을 위해 SharedPreferences에 저장
- 애널리틱스에 로그 기록

**구현 패턴**:
```kotlin
override fun onNewToken(token: String) {
  // 백엔드에 저장 (네트워크 통해)
  userRepository.updateFcmToken(token)

  // 로컬에 저장
  preferences.putString("fcm_token", token)
}
```

---

## mapper/DataToEntity.kt

**목적**: FCM 데이터 페이로드를 도메인 모델로 매핑합니다.

### 매핑 로직

원시 FCM JSON 데이터를 타입이 지정된 도메인 엔티티로 변환합니다.

**입력**: FCM 데이터 맵
```json
{
  "type": "notice",
  "category": "academic_notice",
  "title": "수강신청 안내",
  "content": "2024년 2학기 수강신청...",
  "date": "2024-03-15T10:30:00Z"
}
```

**출력**: 도메인 엔티티 (예: `Notice`, `AcademicEvent`)
```kotlin
Notice(
  id = UUID,
  title = "수강신청 안내",
  content = "2024년 2학기 수강신청...",
  category = NoticeCategory.ACADEMIC,
  createdAt = Instant.parse("2024-03-15T10:30:00Z"),
  source = NoticeSource.FCM
)
```

### 주요 함수

#### mapFcmData(data: Map<String, String>, type: NotificationType): Entity
- 알림 타입에 따라 관련 필드 추출
- 타입 유효성 검사 및 변환 수행
- 누락되거나 잘못된 데이터를 정상적으로 처리
- 저장 준비가 된 도메인 엔티티 반환

**패턴**:
```kotlin
fun mapFcmData(
  data: Map<String, String>,
  type: NotificationType,
): Notice {
  return when (type) {
    NotificationType.ACADEMIC_NOTICE -> Notice(
      title = data["title"] ?: "",
      content = data["content"] ?: "",
      category = parseCategory(data["category"]),
      createdAt = parseDate(data["date"]),
    )
    NotificationType.DEPARTMENT_NOTICE -> /* ... */
    NotificationType.CLUB_NOTICE -> /* ... */
    // ...
  }
}
```

### 오류 처리

- **필수 필드 누락**: 오류 로그 기록, 기본 엔티티 반환
- **잘못된 날짜 형식**: 현재 시간을 폴백으로 사용
- **알 수 없는 타입**: 메시지 버리고 경고 로그 기록

---

## type/NotificationType.kt

**목적**: 지원되는 모든 FCM 알림 타입 정의.

### 열거형 값

```kotlin
enum class NotificationType(val fcmType: String) {
  ACADEMIC_NOTICE("academic_notice"),
  DEPARTMENT_NOTICE("department_notice"),
  CLUB_NOTICE("club_notice"),
  EVENT_NOTICE("event_notice"),
  EMERGENCY_NOTICE("emergency"),
  USER_MESSAGE("message"),
  SYSTEM_ALERT("system_alert"),
  // ... 필요에 따라 추가
}
```

### 메서드

#### fromString(type: String): NotificationType?
FCM 타입 문자열을 열거형으로 안전하게 파싱합니다.

**사용법**:
```kotlin
val type = NotificationType.fromString("academic_notice")
// NotificationType.ACADEMIC_NOTICE 반환
```

#### getCategory(): NoticeCategory
알림 타입을 앱 도메인 카테고리로 매핑합니다.

**예시**:
```kotlin
NotificationType.ACADEMIC_NOTICE.getCategory()
// NoticeCategory.ACADEMIC 반환
```

---

## di/FirebaseMessageModule.kt

**목적**: 메시징 서비스를 위한 Hilt 의존성 주입 설정.

### 제공되는 의존성

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object FirebaseMessageModule {

  @Provides
  @Singleton
  fun provideFirebaseMessaging(): FirebaseMessaging =
    FirebaseMessaging.getInstance()

  @Provides
  @Singleton
  fun provideNotificationType(): NotificationTypeProvider =
    NotificationTypeProvider()
}
```

### 서비스에서의 사용

서비스와 ViewModel은 의존성을 주입받습니다:

```kotlin
@AndroidEntryPoint
class KuringMessagingService : FirebaseMessagingService() {

  @Inject
  lateinit var noticeRepository: NoticeRepository

  @Inject
  lateinit var analyticsManager: AnalyticsManager

  @Inject
  lateinit var preferences: PreferenceUtil

  // onMessageReceived()에서 의존성 사용 가능
}
```

---

## FcmUtil.kt

**목적**: FCM 유틸리티 함수 모음.

### 주요 함수

#### getDeviceToken(): String?
현재 기기의 FCM 토큰을 가져옵니다.

```kotlin
val token = FcmUtil.getDeviceToken()
// 온보딩 또는 프로필 화면에서 사용
```

#### isNotificationEnabled(): Boolean
사용자가 이 앱의 알림을 허용했는지 확인합니다.

#### subscribeToTopic(topic: String)
그룹 메시징을 위해 기기를 FCM 토픽에 구독합니다.

```kotlin
FcmUtil.subscribeToTopic("academic_notices")
// 이제 "academic_notices" 토픽으로 전송된 메시지를 수신합니다
```

#### unsubscribeFromTopic(topic: String)
토픽 구독을 취소합니다.

---

## 메시지 흐름

### 포그라운드 메시지

```text
앱이 열린 상태에서 사용자가 알림 수신
  ↓
FCM이 RemoteMessage 전달
  ↓
KuringMessagingService.onMessageReceived(remoteMessage)
  ↓
DataToEntity.mapFcmData()가 도메인 엔티티 생성
  ↓
레포지토리에 저장 (데이터베이스/캐시)
  ↓
NotificationManager를 통해 알림 생성 및 표시
  ↓
Analytics에 이벤트 로그 기록
  ↓
UI 업데이트 (ViewModel이 레포지토리 Flow를 수신 중인 경우)
```

### 백그라운드 메시지

```text
앱이 닫힌 상태에서 사용자가 알림 수신
  ↓
FCM이 RemoteMessage 전달
  ↓
Android 시스템이 KuringMessagingService 깨움
  ↓
onMessageReceived() 실행
  ↓
데이터베이스에 메시지 저장
  ↓
시스템 알림 트레이에 알림 게시
  ↓
사용자가 알림 탭
  ↓
앱 실행, Activity가 저장된 데이터 읽음
  ↓
새로운 콘텐츠를 표시하도록 UI 업데이트
```

### 토큰 갱신

```text
기기 토큰 만료 또는 새 설치
  ↓
FCM이 새 토큰 생성
  ↓
KuringMessagingService.onNewToken(newToken)
  ↓
백엔드에 토큰 저장
  ↓
로컬 SharedPreferences 업데이트
  ↓
다음 백엔드 메시지가 새 토큰을 타겟팅
```

---

## 설정

### AndroidManifest.xml

FCM에는 다음 권한과 서비스 선언이 필요합니다:

```xml
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<application>
  <service
    android:name="com.ku_stacks.ku_ring.firebase.messaging.KuringMessagingService"
    android:exported="false">
    <intent-filter>
      <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
  </service>
</application>
```

### build.gradle.kts

의존성:

```kotlin
dependencies {
  implementation(libs.firebase.messaging)
  implementation(projects.core.util)  // 유틸리티용
  // Hilt
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
}
```

---

## 통합 체크리스트

FCM 메시징을 기능에 통합할 때:

- [ ] 기능의 `build.gradle.kts`에 `:core:firebase-messaging` 의존성 추가
- [ ] 레포지토리가 `DataToEntity`의 `Notice`/도메인 엔티티를 받을 수 있는지 확인
- [ ] 수신 메시지를 저장할 데이터베이스 스키마 구성
- [ ] UI가 새 메시지를 위해 레포지토리 Flow를 수신하고 있는지 확인
- [ ] 기능의 ViewModel이 첫 실행 시 FCM 토큰을 요청하는지 확인
- [ ] Firebase 콘솔에서 FCM 테스트 메시지 전송
- [ ] 메시지가 수신되고 표시되는지 확인
- [ ] 포그라운드 및 백그라운드 시나리오 모두 테스트

---

## 테스트

### 단위 테스트

`DataToEntity` 매핑 테스트:

```bash
./gradlew :core:firebase-messaging:testDebugUnitTest
```

**테스트 예시**:
```kotlin
class DataToEntityTest {
  @Test
  fun `mapFcmData converts academic notice correctly`() {
    val data = mapOf(
      "type" to "academic_notice",
      "title" to "Test Title",
      "category" to "academic",
    )

    val result = DataToEntity.mapFcmData(data, NotificationType.ACADEMIC_NOTICE)

    assertThat(result.title).isEqualTo("Test Title")
    assertThat(result.category).isEqualTo(NoticeCategory.ACADEMIC)
  }
}
```

### 통합 테스트

1. **Firebase 콘솔**: 기기에 FCM 테스트 메시지 전송
2. **메시지 수신 확인**: 로그 및 데이터베이스 확인
3. **알림 표시 확인**: 시스템 알림 트레이 확인
4. **데이터 저장 확인**: 새 메시지에 대해 데이터베이스 조회

---

## AI 에이전트 안내

### 작업 지침

firebase-messaging 모듈 작업 시:

1. **FCM 생명주기 이해**: 포그라운드 vs 백그라운드 메시지 전달
2. **사용자 개인정보 존중**: Analytics에는 필요한 데이터만 로그 기록
3. **오류를 정상적으로 처리**: 잘못된 메시지로 인해 절대 크래시가 나서는 안 됩니다
4. **철저한 테스트**: 포그라운드 및 백그라운드 시나리오 모두
5. **매핑 로직 단순화**: `DataToEntity`는 순수 함수여야 합니다
6. **타입 정의 문서화**: 모든 `NotificationType` 값에 주석이 있어야 합니다
7. **모든 입력 유효성 검사**: FCM 데이터는 외부에서 오며 신뢰할 수 없습니다

### 테스트 명령어

firebase-messaging 테스트 실행:
```bash
./gradlew :core:firebase-messaging:testDebugUnitTest
```

전체 통합 테스트 (앱 포함):
```bash
./gradlew :app:testDebugUnitTest
```

### 새로운 알림 타입 추가

1. `NotificationType` 열거형에 추가:
   ```kotlin
   SURVEY_REQUEST("survey_request"),
   ```

2. `DataToEntity.mapFcmData()`에 매핑 케이스 추가:
   ```kotlin
   NotificationType.SURVEY_REQUEST -> SurveyRequest(
     id = data["id"] ?: "",
     title = data["title"] ?: "",
     // ... 필드 매핑
   )
   ```

3. `DataToEntityTest`에 단위 테스트 추가

4. 이 파일에 문서화

5. Firebase 콘솔을 통해 엔드투엔드 테스트

### 의존성 주의사항

- **기능/도메인 의존성 없음**: 매핑은 일반적으로 유지하세요
- **레포지토리 주입**: 직접 인스턴스화가 아닌 Hilt 사용
- **싱글톤 서비스**: FCM 토큰과 메시징 서비스는 싱글톤입니다
- **백그라운드 안전성**: `onMessageReceived()`는 백그라운드 스레드에서 실행됩니다; 코루틴을 신중하게 사용하세요

### 일반적인 문제

**메시지가 수신되지 않는 경우**:
- 매니페스트에 서비스가 선언되어 있는지 확인
- app 모듈에 Hilt가 설치되어 있는지 확인
- FCM 토큰이 백엔드에 등록되어 있는지 확인

**중복 메시지**:
- FCM이 동일한 메시지를 두 번 전달할 수 있습니다; 데이터베이스에 멱등적으로 저장하세요

**토큰이 업데이트되지 않는 경우**:
- `onNewToken()`이 호출되는지 확인
- 백엔드가 토큰 업데이트를 수신하는지 확인

**알림이 표시되지 않는 경우**:
- 채널이 생성되었는지 확인 (Android 8 이상)
- 앱에 POST_NOTIFICATIONS 권한이 있는지 확인 (Android 13 이상)

---

## 관련 모듈

- **:core:util** – 유틸리티 및 확장 함수
- **:core:firebase-analytics** – 메시징 이벤트 추적
- **:core:preferences** – 사용자 알림 환경설정 저장
- **:data:notice** – 공지사항 메시지를 데이터베이스에 저장
