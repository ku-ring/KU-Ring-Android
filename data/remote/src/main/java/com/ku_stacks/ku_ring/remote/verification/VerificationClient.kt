package com.ku_stacks.ku_ring.remote.verification

import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import com.ku_stacks.ku_ring.remote.verification.request.SendCodeRequest
import com.ku_stacks.ku_ring.remote.verification.request.VerifyCodeRequest
import javax.inject.Inject

class VerificationClient @Inject constructor(
    private val verificationService: VerificationService,
) {
    suspend fun sendVerificationCode(email: String): DefaultResponse =
        verificationService.sendVerificationCode(SendCodeRequest(email))

    suspend fun verifyVerificationCode(email: String, code: String): DefaultResponse =
        verificationService.verifyVerificationCode(VerifyCodeRequest(email, code))
}
