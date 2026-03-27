package com.voltfitness.app.domain.usecase.user

import com.voltfitness.app.domain.repository.UserRepository
import javax.inject.Inject

class CheckUserExistsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Boolean {
        return userRepository.getUserCount() > 0
    }
}
