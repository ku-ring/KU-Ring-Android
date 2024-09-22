package com.ku_stacks.ku_ring.ai.repository

import com.ku_stacks.ku_ring.domain.KuringBotMessage
import java.time.LocalDate

interface KuringBotRepository {
    /**
     * 쿠링봇 메시지 세션을 열고, 서버로부터 데이터가 주어졌을 때 작업을 실행한다.
     *
     * @param query 질문 내용
     * @param token 사용자 FCM 토큰
     * @param onReceived 데이터를 수신했을 때 실행할 작업
     */
    suspend fun openKuringBotSession(
        query: String,
        token: String,
        onReceived: (String) -> Unit,
    )

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

    // TODO: 질문 횟수를 서버에서 받아오게 구현한 후 삭제
    /**
     * 지정된 기간 동안에 전송한 질문의 수를 반환한다.
     *
     * @param from 기간의 시작
     * @param to 기간의 끝
     * @return [from]부터 [to]까지 전송한 질문의 수 (inclusive)
     */
    suspend fun getQueryCount(from: LocalDate, to: LocalDate): Int

    /**
     * 남은 질문 횟수를 반환한다.
     *
     * @param token 사용자의 FCM 토큰
     */
    suspend fun getQueryCount(token: String): Int
}