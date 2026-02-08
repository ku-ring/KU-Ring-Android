package com.ku_stacks.ku_ring.remote.verification

import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import com.ku_stacks.ku_ring.remote.verification.request.SendCodeRequest
import com.ku_stacks.ku_ring.remote.verification.request.VerifyCodeRequest
import javax.inject.Inject

class VerificationClient @Inject constructor(
    private val verificationService: VerificationService,
) {
    suspend fun sendVerificationCode(request: SendCodeRequest): DefaultResponse<Nothing> =
        verificationService.sendVerificationCode(request)

    suspend fun sendVerificationCodeForPasswordReset(request: SendCodeRequest): DefaultResponse<Nothing> =
        verificationService.sendVerificationCodeForPasswordReset(request)

    suspend fun verifyVerificationCode(request: VerifyCodeRequest): DefaultResponse<Nothing> =
        verificationService.verifyVerificationCode(request)
}
