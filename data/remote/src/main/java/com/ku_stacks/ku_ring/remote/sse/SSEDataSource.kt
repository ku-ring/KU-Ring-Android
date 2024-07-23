package com.ku_stacks.ku_ring.remote.sse

import kotlinx.coroutines.flow.Flow

interface SSEDataSource {
    /**
     * 쿠링봇 SSE 세션을 열고, [Flow] 포맷으로 반환한다.
     *
     * @param query 질문 내용
     * @param token 사용자 FCM 토큰
     *
     * @return 세션을 정상적으로 연 경우 non-null [Flow], 오류가 발생한 경우 null
     */
    suspend fun openKuringBotSSESession(
        query: String,
        token: String,
    ): Flow<String>?
}