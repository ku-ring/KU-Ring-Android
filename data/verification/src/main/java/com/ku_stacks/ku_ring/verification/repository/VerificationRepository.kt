package com.ku_stacks.ku_ring.verification.repository

interface VerificationRepository {
    suspend fun sendVerificationCode(email: String): Result<Unit>
    suspend fun verifyCode(email: String, code: String): Result<Unit>
}