package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ku_stacks.ku_ring.local.entity.KuringBotMessageEntity

@Dao
interface KuringBotMessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: KuringBotMessageEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessages(messages: List<KuringBotMessageEntity>)

    @Query("SELECT * FROM KuringBotMessageEntity ORDER BY id ASC")
    suspend fun getAllMessages(): List<KuringBotMessageEntity>

    /**
     * 일정 기간동안 사용자가 전송한 질문의 수를 반환한다.
     *
     * @param from 기간의 시작 (epoch seconds)
     * @param to 기간의 끝 (epoch seconds)
     *
     * @return 주어진 기간동안 사용자가 전송한 질문의 수
     */
    @Query("SELECT count(*) FROM KuringBotMessageEntity WHERE type = 0 AND postedEpochSeconds BETWEEN :from AND :to")
    suspend fun getQueryCount(from: Long, to: Long): Int

    @Query("DELETE FROM KuringBotMessageEntity")
    suspend fun clear()
}