package com.ku_stacks.ku_ring.verification.repository

import com.ku_stacks.ku_ring.remote.verification.VerificationService
import com.ku_stacks.ku_ring.remote.verification.request.SendCodeRequest
import com.ku_stacks.ku_ring.remote.verification.request.VerifyCodeRequest
import com.ku_stacks.ku_ring.util.suspendRunCatching
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(
    private val verificationService: VerificationService
) : VerificationRepository {
    override suspend fun sendVerificationCode(email: String): Result<Unit> = suspendRunCatching {
        verificationService.sendVerificationCode(
            request = SendCodeRequest(email = email)
        )
    }

    override suspend fun verifyCode(email: String, code: String): Result<Unit> =
        suspendRunCatching {
            verificationService.verifyVerificationCode(
                request = VerifyCodeRequest(
                    email = email,
                    code = code,
                )
            )
        }
}
