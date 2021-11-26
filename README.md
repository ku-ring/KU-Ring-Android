# KU Ring-Android

#### 걱정마, 쿠링이 알려줄게!

## PlayStore 링크 
https://play.google.com/store/apps/details?id=com.ku_stacks.ku_ring

<p align="center">
<img src="https://github.com/KU-Stacks/KU-Ring-Android/blob/main/preview/%EC%BF%A0%EB%A7%81_%EA%B2%80%EC%83%89_%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7.png" width="30%"/>
<img src="https://github.com/KU-Stacks/KU-Ring-Android/blob/main/preview/%EC%BF%A0%EB%A7%81_%EB%82%B4%EC%95%8C%EB%A6%BC_%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7.png" width="30%"/>
<img src="https://github.com/KU-Stacks/KU-Ring-Android/blob/main/preview/%EC%BF%A0%EB%A7%81_%ED%99%88%ED%99%94%EB%A9%B4_%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7.png" width="30%"/>
</p>

## Tech Stack & Libraries

### Architecture
- MVVM Architecture
- Repository Pattern
- Dagger Hilt - 의존성 주입

### Jetpack Library
- Lifecycle(LiveData)
- DataBinding
- ViewModel
- Paging3 + Coroutine Flow
- Room
- Startup - 앱 시작 시 startup 간소화, 초기화 순서 명시
- CoordinatorLayout

### Async
- RxJava (main)
- Coroutine (sub)

### Network
- Gson
- OkHttp3
- Retrofit2 & RxJava3
- java-WebSocket

### other Library
- Timber - Debug 환경일 때는 Log, Release 환경일 때는 Crashlytics
- Firebase Crashlytics
- Firebase Analytics
