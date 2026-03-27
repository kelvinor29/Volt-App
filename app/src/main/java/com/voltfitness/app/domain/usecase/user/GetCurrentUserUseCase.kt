package com.voltfitness.app.domain.usecase.user

import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User?> = userRepository.getCurrentUser()
}