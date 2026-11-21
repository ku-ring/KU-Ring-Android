package com.ku_stacks.ku_ring.remote.util

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 도서관(Pyxis) 서버는 브라우저 요청만 허용하므로, 웹과 동일한 헤더로 요청을 보낸다.
 * 앱 전용 헤더(User-Token, Authorization)는 외부 서버로 보내지 않는다.
 */
class LibraryHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header("Accept", "application/json, text/plain, */*")
            .header("Referer", "https://library.konkuk.ac.kr/")
            .header("User-Agent", BROWSER_USER_AGENT)
            .build()
        return chain.proceed(newRequest)
    }

    private companion object {
        const val BROWSER_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/157.0.0.0 Safari/537.36"
    }
}
