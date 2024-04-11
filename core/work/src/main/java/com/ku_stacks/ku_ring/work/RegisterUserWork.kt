package com.ku_stacks.ku_ring.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.ku_stacks.ku_ring.user.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class RegisterUserWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val token = getToken() ?: return Result.failure()
        userRepository.registerUser(token)
        return Result.success()
    }

    private fun getToken(): String? {
        return inputData.getString(TOKEN_KEY)
    }

    companion object {
        fun createData(token: String): Data {
            return Data.Builder()
                .putString(TOKEN_KEY, token)
                .build()
        }

        private const val TOKEN_KEY = "token-key"
    }
}