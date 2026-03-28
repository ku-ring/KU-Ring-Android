<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# 로컬 데이터베이스 모듈 안내

로컬 데이터베이스 모듈은 오프라인 저장 및 캐싱을 위한 Room 데이터베이스 설정, 엔티티 정의, 타입 컨버터를 제공합니다. SQLite 스키마와 마이그레이션을 관리합니다.

## 모듈 목적

Room 데이터베이스를 통해 앱의 영구 로컬 저장소를 제공합니다. 다음을 가능하게 합니다:
- 캐시된 데이터를 통한 오프라인 기능
- RemoteMediator를 통한 효율적인 페이징
- 데이터 일관성을 위한 트랜잭션 지원
- DAO를 통한 타입 안전 데이터베이스 접근

## 모듈 구조

```
data/local/
├── src/main/java/com/ku_stacks/ku_ring/local/
│   ├── di/
│   │   └── DatabaseModule.kt
│   ├── room/
│   │   ├── KuRingDatabase.kt
│   │   └── *.dao.kt (DAO 인터페이스)
│   ├── entity/
│   │   └── *Entity.kt (Room 엔티티 클래스)
│   └── typeconverter/
│       └── *TypeConverter.kt
├── src/test/
│   └── java/com/ku_stacks/ku_ring/local/
│       └── 테스트 헬퍼 (인메모리 데이터베이스 설정)
└── test/ (헬퍼가 포함된 테스트 모듈)
```

## 모듈 구성

| 서브 모듈 | 목적 |
|-----------|---------|
| **di/** | Database 싱글톤과 DAO 인스턴스를 제공하는 Hilt 모듈 |
| **room/** | Database 클래스와 DAO 인터페이스 정의 |
| **entity/** | 데이터베이스 스키마를 정의하는 Room @Entity 클래스 |
| **typeconverter/** | 커스텀 타입 직렬화를 위한 Room @TypeConverter |
| **test/** | 테스트용 인메모리 데이터베이스 |

## 데이터베이스 모듈

`DatabaseModule.kt`는 Room 데이터베이스와 DAO를 제공합니다:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): KuRingDatabase {
        return Room.databaseBuilder(
            context,
            KuRingDatabase::class.java,
            "ku_ring_db"
        )
        .addMigrations(/* 마이그레이션 */)
        .build()
    }

    @Provides
    fun provideNoticeDao(db: KuRingDatabase): NoticeDao = db.noticeDao()
}
```

## Database 클래스

`KuRingDatabase.kt`는 Room 데이터베이스를 정의합니다:

```kotlin
@Database(
    entities = [
        NoticeEntity::class,
        UserEntity::class,
        DepartmentEntity::class,
        // ... 기타 엔티티
    ],
    version = 1, // 스키마 변경 시 증가
    exportSchema = true
)
@TypeConverters(
    TimeStampConverter::class,
    ListConverter::class,
    // ... 기타 컨버터
)
abstract class KuRingDatabase : RoomDatabase() {
    abstract fun noticeDao(): NoticeDao
    abstract fun userDao(): UserDao
    // ... 기타 DAO 접근자
}
```

스키마 스냅샷은 마이그레이션 추적을 위해 `app/schemas/`에 내보내집니다.

## 엔티티 클래스

엔티티 클래스는 데이터베이스 스키마를 정의합니다:

```kotlin
// entity/NoticeEntity.kt
@Entity(
    tableName = "notices",
    indices = [Index("category"), Index("department_id")]
)
data class NoticeEntity(
    @PrimaryKey
    @ColumnInfo("notice_id")
    val id: String,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("content")
    val content: String,

    @ColumnInfo("category")
    val category: String,

    @ColumnInfo("created_at")
    val createdAt: Long
)
```

주요 어노테이션:
- `@Entity` - 클래스를 데이터베이스 테이블로 지정
- `@PrimaryKey` - 고유 식별자 지정
- `@ColumnInfo` - 속성을 컬럼에 매핑 (선택사항, 속성명으로 자동 감지)
- `@Index` - 쿼리 성능을 위한 데이터베이스 인덱스 생성
- `@ForeignKey` - 참조 무결성 적용

## DAO 인터페이스

Data Access Object는 타입 안전한 데이터베이스 쿼리를 제공합니다:

```kotlin
// room/NoticeDao.kt
@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<NoticeEntity>)

    @Query("SELECT * FROM notices WHERE category = :category ORDER BY created_at DESC")
    fun getNoticesByCategory(category: String): Flow<List<NoticeEntity>>

    @Query("SELECT * FROM notices WHERE notice_id = :id")
    suspend fun getNoticeById(id: String): NoticeEntity?

    @Delete
    suspend fun deleteNotices(notices: List<NoticeEntity>)

    @Query("DELETE FROM notices WHERE created_at < :timestamp")
    suspend fun deleteOldNotices(timestamp: Long)
}
```

일반적인 DAO 패턴:
- `@Insert` - 레코드 추가
- `@Update` - 레코드 수정
- `@Delete` - 레코드 삭제
- `@Query` - 커스텀 SQL 쿼리
- `suspend` - 단일 실행 작업용
- `Flow<T>` - 관찰 가능한 쿼리용

## 타입 컨버터

타입 컨버터는 저장을 위해 복잡한 타입을 직렬화합니다:

```kotlin
// typeconverter/TimeStampConverter.kt
object TimeStampConverter {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(it),
                ZoneId.systemDefault()
            )
        }
    }
}

// typeconverter/ListConverter.kt
object ListConverter {
    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        return value?.let { Json.decodeFromString(it) }
    }
}
```

## Room 마이그레이션

스키마 변경 시 마이그레이션이 필요합니다 (`DatabaseModule.kt` 내):

```kotlin
val migration1To2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 기존 테이블에 새 컬럼 추가
        database.execSQL(
            "ALTER TABLE notices ADD COLUMN priority INTEGER DEFAULT 0"
        )
    }
}

val migration2To3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 새 테이블 생성
        database.execSQL("""
            CREATE TABLE departments(
                department_id TEXT PRIMARY KEY,
                name TEXT NOT NULL
            )
        """)
    }
}

Room.databaseBuilder(context, KuRingDatabase::class.java, "ku_ring_db")
    .addMigrations(migration1To2, migration2To3)
    .build()
```

스키마 변경 시:
1. 엔티티 클래스 업데이트
2. KuRingDatabase의 `@Database(version = x)` 버전 증가
3. 마이그레이션 객체 생성: `Migration(oldVersion, newVersion)`
4. DatabaseModule의 `addMigrations()`에 마이그레이션 추가
5. `./gradlew :app:build` 실행하여 스키마 스냅샷 생성

## 테스트

테스트 모듈은 인메모리 데이터베이스 설정을 제공합니다:

```kotlin
// 테스트에서
@get:Rule
val instantExecutorRule = InstantTaskExecutorRule()

private lateinit var database: KuRingDatabase
private lateinit var noticeDao: NoticeDao

@Before
fun setup() {
    database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        KuRingDatabase::class.java
    ).allowMainThreadQueries().build()

    noticeDao = database.noticeDao()
}

@Test
fun testInsertAndQuery() = runTest {
    val notice = NoticeEntity(
        id = "1",
        title = "Test Notice",
        // ...
    )
    noticeDao.insertNotices(listOf(notice))

    val result = noticeDao.getNoticeById("1")
    assertEquals("Test Notice", result?.title)
}

@After
fun tearDown() {
    database.close()
}
```

테스트 실행:

```bash
# 로컬 데이터베이스 테스트
./gradlew :data:local:test

# 전체 데이터 레이어 테스트
./gradlew :data:test
```

## 일반 작업

### 새 엔티티와 DAO 추가

1. `entity/`에 엔티티 클래스 생성:
   ```kotlin
   @Entity(tableName = "clubs")
   data class ClubEntity(
       @PrimaryKey val id: String,
       @ColumnInfo("name") val name: String,
       // ... 기타 필드
   )
   ```

2. `room/`에 DAO 인터페이스 생성:
   ```kotlin
   @Dao
   interface ClubDao {
       @Insert(onConflict = OnConflictStrategy.REPLACE)
       suspend fun insertClubs(clubs: List<ClubEntity>)

       @Query("SELECT * FROM clubs")
       fun getAllClubs(): Flow<List<ClubEntity>>
   }
   ```

3. `KuRingDatabase.entities`에 엔티티 추가:
   ```kotlin
   @Database(
       entities = [
           NoticeEntity::class,
           ClubEntity::class, // 여기에 추가
       ],
       version = 2 // 버전 증가
   )
   ```

4. `DatabaseModule.kt`에 마이그레이션 생성:
   ```kotlin
   val migration1To2 = object : Migration(1, 2) {
       override fun migrate(database: SupportSQLiteDatabase) {
           database.execSQL("""
               CREATE TABLE clubs(
                   id TEXT PRIMARY KEY,
                   name TEXT NOT NULL
               )
           """)
       }
   }
   ```

5. Database에 DAO 접근자 추가:
   ```kotlin
   abstract fun clubDao(): ClubDao
   ```

6. `./gradlew :app:build` 실행하여 스키마 스냅샷 생성

### 타입 컨버터 추가

1. `typeconverter/`에 컨버터 클래스 생성:
   ```kotlin
   object MyCustomTypeConverter {
       @TypeConverter
       fun fromCustomType(value: MyCustomType?): String? {
           return value?.let { /* 직렬화 */ }
       }

       @TypeConverter
       fun toCustomType(value: String?): MyCustomType? {
           return value?.let { /* 역직렬화 */ }
       }
   }
   ```

2. `KuRingDatabase`의 @TypeConverters에 추가:
   ```kotlin
   @TypeConverters(
       TimeStampConverter::class,
       ListConverter::class,
       MyCustomTypeConverter::class // 여기에 추가
   )
   ```

3. 단위 테스트에서 직렬화/역직렬화 테스트

### 페이징으로 쿼리

레포지토리에서 DAO와 Paging3를 조합:

```kotlin
// 레포지토리에서
override fun getNotices(): Flow<PagingData<Notice>> {
    return Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = CategoryNoticeMediator(
            remoteDataSource,
            localDataSource
        ),
        pagingSourceFactory = {
            noticeDao.getNoticesByCategory(category).map { /* 변환 */ }
        }
    ).flow
}
```

RemoteMediator는 API에서 데이터베이스로 데이터를 로드하고, DAO는 페이징된 결과를 스트리밍합니다.

## AI 에이전트 안내

**주요 책임**:
- 엔티티는 스키마를 정의합니다. 변경 시 마이그레이션과 버전 증가가 필요합니다.
- DAO는 타입 안전한 쿼리를 제공합니다. 관찰 가능한 데이터에는 Flow를 사용하세요.
- 타입 컨버터는 양방향이어야 합니다 (올바르게 직렬화 및 역직렬화).
- 엔티티와 DAO 변경은 항상 인메모리 데이터베이스로 테스트하세요.
- `app/schemas/`의 스키마 스냅샷은 마이그레이션 이력을 추적합니다.

**자주 묻는 질문**:
- "새 테이블은 어떻게 정의하나요?" - entity/에 엔티티 생성, @Database에 추가, 마이그레이션 생성
- "데이터베이스 쿼리는 어떻게 테스트하나요?" - `Room.inMemoryDatabaseBuilder()`로 인메모리 데이터베이스 사용
- "커스텀 타입은 어떻게 저장하나요?" - typeconverter/에 @TypeConverter 생성
- "Flow로 쿼리하는 방법은?" - DAO 메서드가 `Flow<List<Entity>>`를 반환
- "마이그레이션은 어디에 있나요?" - `DatabaseModule.kt`에서 `addMigrations()`에 전달

**테스트 방법**:
- 코루틴 테스트에는 `InstantTaskExecutorRule` 사용
- 테스트 편의를 위해 `allowMainThreadQueries()` 사용
- CRUD 작업 테스트 (삽입, 조회, 수정, 삭제)
- 쿼리 필터 및 정렬 테스트
- 타입 컨버터의 직렬화/역직렬화 검증

**주의사항**: 스키마 변경은 모든 클라이언트에 영향을 줍니다. 항상 마이그레이션을 생성하고 버전을 증가시킨 후 `./gradlew :data:local:test`로 테스트하세요.
