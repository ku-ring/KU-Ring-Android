package com.ku_stacks.ku_ring.remote.verification

import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import com.ku_stacks.ku_ring.remote.verification.request.SendCodeRequest
import com.ku_stacks.ku_ring.remote.verification.request.VerifyCodeRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface VerificationService {
    @POST("v2/verification-code")
    suspend fun sendVerificationCode(
        @Body request: SendCodeRequest,
    ): DefaultResponse

    @POST("v2/verification-code/verify")
    suspend fun verifyVerificationCode(
        @Body request: VerifyCodeRequest,
    ): DefaultResponse
}
