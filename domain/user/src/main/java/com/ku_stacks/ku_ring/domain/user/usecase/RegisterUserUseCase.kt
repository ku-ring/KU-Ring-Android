package com.ku_stacks.ku_ring.domain.user.usecase

import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> = runCatching {
        userRepository.registerUser(token)
    }
}
