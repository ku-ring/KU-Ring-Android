package com.ku_stacks.ku_ring.remote.kuringbot

interface KuringBotClient {
    /**
     * 쿠링봇 연결을 만들고, 응답을 받아 문자열 형식으로 처리한다.
     *
     * @param query 질문 내용
     * @param token 사용자 FCM 토큰
     * @param onReceived 데이터를 수신했을 때 실행할 작업.
     */
    suspend fun openKuringBotConnection(
        query: String,
        token: String,
        onReceived: (String) -> Unit,
    )
}