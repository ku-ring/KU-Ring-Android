package com.ku_stacks.ku_ring.ai.repository

import com.ku_stacks.ku_ring.domain.KuringBotMessage

interface KuringBotRepository {
    /**
     * 쿠링봇 메시지 세션을 열고, SSE 데이터와 그 외의 데이터가 왔을 때 각각 다른 작업을 수행한다.
     *
     * @param query 질문 내용
     * @param token 사용자 FCM 토큰
     * @param onSSEData SSE 데이터를 수신했을 때 실행할 작업
     * @param onOtherData SSE 외의 데이터를 수신했을 때 실행할 작업
     */
    suspend fun openKuringBotSession(
        query: String,
        token: String,
        onSSEData: (String) -> Unit,
        onOtherData: (String) -> Unit,
    )

    /**
     * 쿠링봇 메시지 세션을 열고, 데이터가 왔을 때 작업을 수행한다.
     *
     * @param query 질문 내용
     * @param token 사용자 FCM 토큰
     * @param onData 데이터를 수신했을 때 실행할 작업
     */
    suspend fun openKuringBotSession(
        query: String,
        token: String,
        onData: (String) -> Unit,
    ) = openKuringBotSession(query, token, onData, onData)

    /**
     * 로컬 DB에 저장된 쿠링봇 메시지를 모두 반환한다.
     *
     * @return 로컬에 저장된 모든 쿠링봇 메시지
     */
    suspend fun getAllMessages(): List<KuringBotMessage>

    /**
     * 로컬 DB에 쿠링봇 메시지 여러 개를 저장한다.
     * 이미 저장되어 있는 메시지는 중복으로 저장되지 않는다.
     *
     * @param messages 로컬에 저장할 메시지
     */
    suspend fun insertMessages(messages: List<KuringBotMessage>)

    /**
     * 로컬 DB에 쿠링봇 메시지를 저장한다.
     * 이미 저장되어 있는 메시지라면, 아무 일도 일어나지 않는다.
     *
     * @param message 로컬에 저장할 메시지
     */
    suspend fun insertMessage(message: KuringBotMessage)

    /**
     * 로컬 DB에 저장된 쿠링봇 메시지를 모두 삭제한다.
     */
    suspend fun clear()
}