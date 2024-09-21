<img src="https://github.com/KU-Stacks/KU-Ring-Android/blob/main/preview/concat.png" alt="쿠링 소개 이미지"/>

<a href='https://play.google.com/store/apps/details?id=com.ku_stacks.ku_ring'><img alt='Google Play에서 쿠링 다운로드' width=250 src='https://play.google.com/intl/ko/badges/static/images/badges/ko_badge_web_generic.png'/></a>

# KU Ring Android

### 걱정마, 쿠링이 알려줄게!

건국대학교 공지 앱 쿠링의 안드로이드 앱 리포지토리입니다.

# Tech Stack

## Architecture
- Jetpack Compose w/ MVVM
- Unidirectional Data Flow
- Dependency injection by Hilt
- Modularization: `app`, `feature`, `data`, `common`

## Android Jetpack
- ViewModel
- Room
- Lifecycle
- Paging3
- Startup
- WorkManager
- Navigation w/ Compose

## Async
- Kotlin Coroutines

## Network Requests
- Retrofit2
- Gson

## other Libraries
- Firebase
  - Crashlytics
  - Analytics
  - Remote Messaging
- LeakCanary
- [SemVer](https://github.com/semver/semver)

## Test
- Junit4
- Robolectric
- Mockito
- SmokeTest

## Module Graph 생성 방법

```shell
# macOS 기준
# 1. 그래프를 시각화하는 오픈소스 설치
brew install graphviz

# 그래프 생성 Gradle Task 실행
./gradlew projectDependencyGraph

# windows
# 설치: https://graphviz.org/download/

# 그래프 생성 Gradle Task 실행
gradlew projectDependencyGraph
```

# Contact

[![Email](https://img.shields.io/badge/kuring.korea@gmail.com-168de2?style=for-the-badge&logo=gmail&logoColor=white)](mailto:kuring.korea@gmail.com)

[![Instagram](https://img.shields.io/badge/@kuring.konkuk-e4405f?style=for-the-badge&logo=instagram&logoColor=white)](https://bit.ly/3JyMWMi)