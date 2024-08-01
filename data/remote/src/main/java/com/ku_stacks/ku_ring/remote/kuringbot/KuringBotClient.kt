package com.ku_stacks.ku_ring.remote.kuringbot

interface KuringBotClient {
    /**
     * 쿠링봇 SSE 세션을 열고, 데이터를 문자열 형식으로 받아 처리한다.
     * SSE 데이터를 받았을 때, [onReceived]에 접두어 `data:`를 떼지 않고 그대로 제공한다.
     *
     * @param query 질문 내용
     * @param token 사용자 FCM 토큰
     * @param onReceived 데이터를 수신했을 때 실행할 작업.
     */
    suspend fun openKuringBotSSESession(
        query: String,
        token: String,
        onReceived: (String) -> Unit,
    )
}