<!-- Parent: ../AGENTS.md -->
<!-- Generated: 2026-03-29 | Updated: 2026-03-29 -->

# GitHub 설정: AI 에이전트 안내

KU-Ring Android의 GitHub Actions CI/CD 워크플로우 및 저장소 설정 디렉토리입니다. push 및 pull request 이벤트에서 트리거되는 자동화된 빌드, 테스트, 릴리즈 파이프라인이 포함되어 있습니다.

---

## 디렉토리 구조

```text
.github/
├── workflows/
│   ├── android-develop.yml          # develop 브랜치용 CI 워크플로우 (빌드 + 유닛 테스트)
│   ├── android-release.yml          # release 브랜치용 CD 워크플로우 (빌드 + 배포)
│   └── auto-delete-merged-branches.yml # 병합된 브랜치 자동 삭제 워크플로우
├── auto_assign.yml                  # PR 자동 담당자 지정 설정
└── AGENTS.md                        # 이 파일
```

---

## 주요 파일

| 파일 | 설명 |
|------|---------|
| `workflows/android-develop.yml` | **develop 브랜치 CI**: develop 브랜치에 PR 또는 push 시 트리거. 디버그 APK 빌드 및 유닛 테스트 실행. 테스트 미통과 시 PR 차단. |
| `workflows/android-release.yml` | **release 브랜치 CD**: release/* 브랜치에 push/PR 시 트리거. 릴리즈 APK 및 AAB 빌드, 테스트 실행, 아티팩트 업로드. 시크릿이 필요합니다. |
| `workflows/auto-delete-merged-branches.yml` | **정리 워크플로우**: 병합된 기능 브랜치를 자동으로 삭제합니다. |
| `auto_assign.yml` | **PR 자동 담당자 지정**: 새 pull request에 리뷰어를 자동으로 할당하는 설정. |

---

## CI/CD 워크플로우

### develop 브랜치 워크플로우 (`android-develop.yml`)

**트리거**: `develop` 브랜치에 push 또는 PR

**단계**:
1. 저장소 체크아웃 (actions/checkout@v6)
2. JDK 21 설정 (Temurin 배포판, Gradle 캐시 포함)
3. 실행 권한 부여: `chmod +x gradlew`
4. `GOOGLE_SERVICES_JSON_DEBUG` 시크릿으로 `google-services.json` (디버그) 생성
5. `LOCAL_PROPERTIES` 시크릿으로 `local.properties` 생성
6. 디버그 APK 빌드: `./gradlew :app:assembleDebug`
7. 유닛 테스트 실행: `./gradlew testDebugUnitTest`

**결과**:
- **PASS**: 모든 단계가 오류 없이 완료된 경우
- **FAIL**: 빌드 또는 테스트 실패 시 (PR 병합 차단)

**아티팩트**: 보관 없음 (디버그 빌드 전용)

### release 브랜치 워크플로우 (`android-release.yml`)

**트리거**: `release/*` 브랜치에 push, `release/*`로 PR, 또는 수동 `workflow_dispatch`

**단계**:
1. 저장소 체크아웃
2. JDK 21 설정 (Temurin, Gradle 캐시 포함)
3. 실행 권한 부여: `chmod +x gradlew`
4. `GOOGLE_SERVICES_JSON_RELEASE` 시크릿으로 `google-services.json` (릴리즈) 생성
5. 키스토어 및 프로퍼티 생성:
   - `KU_RING_KEY_STORE_JKS` (base64) 디코딩 → `./app/signing/ku_ring_keystore.jks`
   - `KEY_STORE_PROPERTIES` 작성 → `./app/signing/keystore.properties`
6. `LOCAL_PROPERTIES` 시크릿으로 `local.properties` 생성
7. 유닛 테스트 실행: `./gradlew testReleaseUnitTest`
8. 릴리즈 APK 빌드: `./gradlew :app:assembleRelease`
9. 릴리즈 AAB 빌드 (Google Play): `./gradlew :app:bundleRelease`
10. APK 아티팩트 업로드 (14일 보관)
11. AAB 아티팩트 업로드 (14일 보관)

**결과**:
- **PASS**: 모든 단계 완료 및 아티팩트 업로드된 경우
- **FAIL**: 테스트 또는 빌드 실패 시

**아티팩트**:
- `app-release.apk` — 직접 배포용 설치 가능한 APK
- `app-release.aab` — Google Play 스토어용 Android App Bundle

---

## 필수 GitHub 시크릿

워크플로우가 성공하려면 **Settings → Secrets and variables → Actions**에서 다음 시크릿을 설정해야 합니다:

| 시크릿 | 워크플로우 | 용도 |
|--------|----------|---------|
| `GOOGLE_SERVICES_JSON_DEBUG` | android-develop.yml | 디버그 빌드용 Firebase 설정 (JSON 문자열) |
| `GOOGLE_SERVICES_JSON_RELEASE` | android-release.yml | 릴리즈 빌드용 Firebase 설정 (JSON 문자열) |
| `LOCAL_PROPERTIES` | 양쪽 모두 | 로컬 Gradle 프로퍼티 (API 키, SDK 경로 등) |
| `KU_RING_KEY_STORE_JKS` | android-release.yml | base64로 인코딩된 서명 키스토어 |
| `KEY_STORE_PROPERTIES` | android-release.yml | 키스토어 비밀번호 및 alias 프로퍼티 |

**형식**:
- `GOOGLE_SERVICES_JSON_*`: 원본 JSON 파일 내용을 문자열로 붙여넣기
- `LOCAL_PROPERTIES`: Key=value 쌍, 줄바꿈 구분
- `KU_RING_KEY_STORE_JKS`: base64로 인코딩된 바이너리 키스토어 파일 (`cat file.jks | base64`)
- `KEY_STORE_PROPERTIES`: Key=value 쌍 (storePassword, keyPassword, keyAlias)

---

## 하위 디렉토리

| 디렉토리 | 설명 |
|-----------|---------|
| `workflows/` | GitHub Actions 워크플로우 YAML 파일. 각 워크플로우는 지정된 트리거에 의해 자동으로 실행됩니다. |

---

## AI 에이전트 안내

### 새 워크플로우 추가

새 GitHub Actions 워크플로우를 생성하려면:

1. **파일 생성**: `/.github/workflows/my-workflow.yml`
2. **표준 구조 사용**:
   ```yaml
   name: My Workflow

   on:
     push:
       branches:
         - develop
     pull_request:
       branches:
         - develop

   jobs:
     build:
       runs-on: ubuntu-latest
       steps:
         - uses: actions/checkout@v6
         - name: Set up JDK 21
           uses: actions/setup-java@v5
           with:
             java-version: '21'
             distribution: 'temurin'
             cache: gradle
         - name: Build
           run: ./gradlew build
   ```

3. **로컬 테스트** (선택사항): `act` 도구로 워크플로우를 로컬에서 시뮬레이션
4. **커밋 및 push**: 다음 트리거 이벤트 발생 시 워크플로우가 자동으로 실행됨
5. **모니터링**: GitHub Actions 탭에서 워크플로우 실행 및 로그 확인

### 워크플로우 실패 디버깅

1. **로그 확인**: GitHub UI → Actions → 워크플로우 실행 → 단계별 로그
2. **일반적인 문제**:
   - **시크릿 누락**: 시크릿 이름이 워크플로우 참조와 일치하는지 확인 (대소문자 구분)
   - **Gradle 캐시 문제**: `cache: gradle`를 제거하고 재실행
   - **JDK 버전 불일치**: 프로젝트는 JDK 21이 필요하며, setup-java@v5에서 `java-version: '21'` 지정 확인
   - **서명 실패**: 키스토어 시크릿이 base64로 인코딩되었는지, 잘리지 않았는지 확인

3. **실패한 작업 재실행**: GitHub UI의 "Re-run failed jobs" 버튼 사용

### 기존 워크플로우 수정

**`android-develop.yml`을 업데이트할 때**:
- 새 빌드 단계 추가 (예: lint 검사)
- 유닛 테스트 명령어 변경
- 새 Android SDK 버전 추가

**`android-release.yml`을 업데이트할 때**:
- 새 빌드 단계 추가
- 아티팩트 보관 정책 변경
- Play Store 배포 단계 추가

**예시: Lint 검사 추가**
```yaml
- name: Run Lint
  run: ./gradlew :app:lintDebug
```

### 자주 사용하는 워크플로우 패턴

**조건부 단계** (예: release 브랜치에서만 실행):
```yaml
- name: Build Release
  run: ./gradlew assembleRelease
  if: startsWith(github.ref, 'refs/heads/release/')
```

**아티팩트 업로드**:
```yaml
- name: Upload Artifact
  uses: actions/upload-artifact@v7.0.0
  with:
    name: my-artifact
    path: build/outputs/**/*.apk
    retention-days: 7
```

**Slack/Discord 알림** (빌드 후):
```yaml
- name: Notify on Failure
  if: failure()
  run: |
    curl -X POST $WEBHOOK_URL -d '{"text":"Build failed"}'
```

---

## 테스트 요구사항

**워크플로우를 수정하는 에이전트의 경우:**

1. **문법 검증**: 워크플로우는 커밋 push 시 자동으로 검증됨
2. **트리거 테스트**: 워크플로우 트리거에 맞는 테스트 브랜치 생성 (예: PR 트리거라면 `test/my-feature`)
3. **실행 모니터링**: GitHub Actions 탭에서 실행 및 로그 확인
4. **성공 확인**: 모든 단계 통과 및 아티팩트(있는 경우) 생성 확인
5. **로그 검토**: 단계별 출력에서 오류 또는 경고 확인

**새 워크플로우 단계를 추가하는 에이전트의 경우:**

1. 가능하면 로컬 테스트: `act -l` (워크플로우 목록), `act` (실행)
2. 유사한 기존 단계 뒤에 새 단계 추가 (예: lint 다음에 빌드)
3. 표준 액션 사용: `checkout@v6`, `setup-java@v5`, `upload-artifact@v7`
4. 의미 있는 단계 이름과 오류 메시지 포함
5. develop에 병합하기 전에 기능 브랜치에서 테스트

---

## 워크플로우 참조

**사용되는 표준 액션**:
- `actions/checkout@v6` — 저장소 클론
- `actions/setup-java@v5` — Java/JDK 설정
- `actions/upload-artifact@v7` — 빌드 아티팩트 저장

**사용되는 Gradle 명령어**:
- `./gradlew assembleDebug` — 디버그 APK 빌드
- `./gradlew assembleRelease` — 릴리즈 APK 빌드
- `./gradlew bundleRelease` — App Bundle (AAB) 빌드
- `./gradlew testDebugUnitTest` — 유닛 테스트 실행 (디버그)
- `./gradlew testReleaseUnitTest` — 유닛 테스트 실행 (릴리즈)

---

## 관련 문서

- 상위 가이드: `../AGENTS.md` — 전체 프로젝트 구조 및 컨벤션
- 릴리즈 프로세스: 루트 AGENTS.md의 "Releasing" 섹션 참고
- 시크릿 관리: GitHub Settings → Security → Secrets and variables
